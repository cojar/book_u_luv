package com.example.demo.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import com.example.demo.web.service.articleService;
import org.springframework.ui.Model;
import com.example.demo.web.dto.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class articleController {

    private final articleService articleService;

    @GetMapping("/articles")
    public String list(Model model) {
        List<articleDto> articleList = this.articleService.getAll();
        model.addAttribute("articleList", articleList);
        return "article_list";
    }

    @GetMapping("/articles/{id}")
    private String detail() {
        return "";
    }

    @PostMapping("/articles/create")
    private String create() {
        return "";
    }

}
