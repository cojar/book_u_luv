package com.project.bookuluv.app.admin.controller;

import com.project.bookuluv.app.admin.dto.NoticeDto;
import com.project.bookuluv.app.admin.service.NoticeService;
import com.project.bookuluv.app.article.domain.Article;
import com.project.bookuluv.app.article.domain.Notice;
import com.project.bookuluv.app.article.dto.ArticleDto;
import com.project.bookuluv.member.domain.Member;
import com.project.bookuluv.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    // @PreAuthorize("isAuthenticated()")
    public String noticeCreate(@Valid NoticeDto noticeDto, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "notice/notice_form";
        }

        Member member = this.memberService.getMember(principal.getName());
        this.noticeService.create(noticeDto.getSubject(), noticeDto.getContent(), member);
        return "redirect:/";
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
    // @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable("id") Integer id, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "notice/list";
        }
        Notice notice = this.noticeService.getById(id);
        this.noticeService.delete(notice);
        return "redirect:/";
    }
}
