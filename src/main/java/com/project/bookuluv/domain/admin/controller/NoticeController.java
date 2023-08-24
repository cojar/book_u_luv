package com.project.bookuluv.domain.admin.controller;

import com.project.bookuluv.domain.admin.domain.Notice;
import com.project.bookuluv.domain.admin.dto.NoticeDto;
import com.project.bookuluv.domain.admin.service.NoticeService;
import com.project.bookuluv.domain.member.domain.Member;
import com.project.bookuluv.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    private final MemberService memberService;

    private final ADMController admController;

    @GetMapping("/list")
    public String noticeList(Model model) {
        List<Notice> noticeList = this.noticeService.getAll();
        model.addAttribute("noticeList", noticeList);
        return "notice/list";
    }

    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(NoticeDto noticeDto, Model model) {
        if (!admController.userIsAdmin()) { // 모든 관리자권한 가능
            throw new AccessDeniedException("Access is denied");
        }
        String write = "공지사항 작성";
        model.addAttribute("pageTitle", write);
        return "notice/notice_form";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String noticeCreate(@Valid NoticeDto noticeDto, BindingResult bindingResult, Principal principal) {
        if (!admController.userIsAdmin()) { // 모든 관리자권한 가능
            throw new AccessDeniedException("Access is denied");
        }
        if (bindingResult.hasErrors()) {
            return "notice/notice_form";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Member member = this.memberService.getMember(userName);
        this.noticeService.create(noticeDto.getSubject(), noticeDto.getContent(), member);
        return "redirect:/notice/list?success=create";
    }


    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modify(NoticeDto noticeDto, @PathVariable("id") Long id, Principal principal, Model model) {
        if (!admController.userIsAdmin()) { // 모든 관리자권한 가능
            throw new AccessDeniedException("Access is denied");
        }

        Notice notice = this.noticeService.getById(id);
        if (!notice.getRegister().getUserName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        if (notice == null) {
            return "error_page";
        }
        noticeDto.setSubject(notice.getSubject());
        noticeDto.setContent(notice.getContent());
        String write = "공지사항 수정";
        model.addAttribute("pageTitle", write);

        return "notice/notice_form";
    }

    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String noticeModify(@Valid NoticeDto noticeDto,
                               @PathVariable("id") Long id,
                               Principal principal,
                               BindingResult bindingResult) {
        if (!admController.userIsAdmin()) { // 모든 관리자권한 가능
            throw new AccessDeniedException("Access is denied");
        }
        Notice notice = this.noticeService.getById(id);
        if (!notice.getRegister().getUserName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        if (bindingResult.hasErrors()) {
            return "notice/notice_form";
        }
        this.noticeService.modify(noticeDto, notice.getId());
        return "redirect:/notice/detail/" + notice.getId() + "?success=modify";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable("id") Long id) {
        if (!admController.userIsAdmin()) { // 모든 관리자권한 가능
            throw new AccessDeniedException("Access is denied");
        }
        Notice notice = this.noticeService.getById(id);
        this.noticeService.delete(notice);
        return "redirect:/notice/list";
    }

    @GetMapping("/detail/{id}")
    public String noticeDetail(@PathVariable("id") Long id, Model model) {
        Notice notice = noticeService.getById(id);
        model.addAttribute("notice", notice);
        return "notice/detail";  // 상세 페이지의 Thymeleaf 템플릿 이름
    }
}
