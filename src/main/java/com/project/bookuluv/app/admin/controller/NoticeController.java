package com.project.bookuluv.app.admin.controller;

import com.project.bookuluv.app.admin.service.NoticeService;
import com.project.bookuluv.app.article.domain.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/list")
    public String noticeList(Model model) {
        List<Notice> noticeList = this.noticeService.getAll();
        model.addAttribute("noticeList", noticeList);
        return "notice";
    }


}
