package com.project.bookuluv.app.article.controller;

import com.project.bookuluv.app.article.domain.Article;
import com.project.bookuluv.app.article.service.ArticleService;
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
        return "article_list";
    }

    @GetMapping(value = "/article/detail/{id}")
    private String detail(Model model, @PathVariable("id") Integer id) {
        Article article = this.articleService.getById(id);
        model.addAttribute("article", article);
        return "article_detail";
    }
}
