package com.project.bookuluv.app.article.controller;

import com.project.bookuluv.app.article.domain.Article;
import com.project.bookuluv.app.article.dto.ArticleDto;
import com.project.bookuluv.app.article.service.ArticleService;
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
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    private final MemberService memberService;









    @PostMapping("/c")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable("id") Integer id, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "article_list";
        }
        Article article = this.articleService.getById(id);
        this.articleService.delete(article);
        return "redirect:/";
    }
}