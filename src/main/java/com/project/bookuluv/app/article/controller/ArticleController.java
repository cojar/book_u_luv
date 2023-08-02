package com.project.bookuluv.app.article.controller;

import com.project.bookuluv.app.article.domain.Article;
import com.project.bookuluv.app.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/articles")
    public String list(Model model) {
        List<Article> articleList = this.articleService.getAll();
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
