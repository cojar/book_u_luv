package com.project.bookuluv.domain.article.controller;

import com.project.bookuluv.domain.article.service.ArticleService;
import com.project.bookuluv.domain.article.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/article/list")
    public String marketList(Model model) {
        List<Article> articleList = this.articleService.getAll();
        model.addAttribute("articleList", articleList);
        return "article/list";
    }

    @GetMapping(value = "/article/detail/{id}")
    private String detail(Model model, @PathVariable("id") Integer id) {
        Article article = this.articleService.getById(id);
        model.addAttribute("article", article);
        return "article/detail";
    }
}
