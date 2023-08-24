package com.project.bookuluv.domain.admin.controller;

import com.project.bookuluv.domain.admin.domain.Notice;
import com.project.bookuluv.domain.admin.dto.NoticeDto;
import com.project.bookuluv.domain.admin.service.NoticeService;
import com.project.bookuluv.domain.member.domain.Member;
import com.project.bookuluv.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    private final MemberService memberService;

    @GetMapping("/list")
    public String noticeList(Model model) {
        List<Notice> noticeList = this.noticeService.getAll();
        model.addAttribute("noticeList", noticeList);
        return "notice/list";
    }

    @GetMapping("/create")
    // @PreAuthorize("isAuthenticated()")
    public String create(NoticeDto noticeDto) {
        return "notice/notice_form";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String noticeCreate(@Valid NoticeDto noticeDto, BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            return "notice/notice_form";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Member member = this.memberService.getMember(userName);
        this.noticeService.create(noticeDto.getSubject(), noticeDto.getContent(), member);
        return "redirect:/admin/notice";
    }


    @GetMapping("/modify/{id}")
    // @PreAuthorize("isAuthenticated()")
    public String modify(@PathVariable("id") Integer id, Principal principal) {
        return "notice/notice_form";
    }

    @PostMapping("/modify/{id}")
    // @PreAuthorize("isAuthenticated()")
    public String noticeModify(@PathVariable("id") Integer id, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "notice/notice_form";
        }

        Notice notice = this.noticeService.getById(id);
        this.noticeService.modify(notice.getSubject(), notice.getContent(), notice);
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable("id") Integer id, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "notice/list";
        }
        Notice notice = this.noticeService.getById(id);
        this.noticeService.delete(notice);
        return "redirect:/";
    }
}
