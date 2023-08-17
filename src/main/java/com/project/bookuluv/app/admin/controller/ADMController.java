package com.project.bookuluv.app.admin.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ADMController {

    private final ArticleService articleService;

    private final MemberService memberService;

    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    private String create() {
        return "article_form";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    private String articleCreate(@Valid ArticleDto articleDto, BindingResult bindingResult, Principal principal, @RequestParam("files") MultipartFile[] files) throws IOException {
        if (bindingResult.hasErrors()) {
            return "article_form";
        }

        Member member = this.memberService.getMember(principal.getName());
        this.articleService.create(articleDto.getSubject(), articleDto.getContent(), member, files);
        return "redirect:/";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Article> articleList = this.articleService.getAll();
        model.addAttribute("articleList", articleList);
        return "article_list";
    }

    @GetMapping(value = "/detail/{id}")
    private String detail(Model model, @PathVariable("id") Integer id) {
        Article article = this.articleService.getById(id);
        model.addAttribute("article", article);
        return "article_detail";
    }

    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modify(@PathVariable("id") Integer id, Principal principal) {
        return "/article/article_form";
    }

    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String articleModify(@PathVariable("id") Integer id, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "/article/article_form";
        }

        Article article = this.articleService.getById(id);
        this.articleService.modify(article.getSubject(), article.getContent(), article);
        return "redirect:/";
    }

}
