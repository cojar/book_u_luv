package com.project.bookuluv.domain.admin.controller;

import com.project.bookuluv.domain.admin.domain.Notice;
import com.project.bookuluv.domain.admin.service.ProductService;
import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.admin.service.NoticeService;
import com.project.bookuluv.domain.article.domain.Article;
import com.project.bookuluv.domain.article.dto.ArticleDto;
import com.project.bookuluv.domain.article.service.ArticleService;
import com.project.bookuluv.domain.member.domain.Member;
import com.project.bookuluv.domain.member.service.MemberService;
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

    private final NoticeService noticeService;

    private final ProductService productService;

    @GetMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    private String create() {
        return "article_form";
    }

    @PostMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    private String articleCreate(@Valid ArticleDto articleDto, BindingResult bindingResult, Principal principal, @RequestParam("files") MultipartFile[] files) throws IOException {
        if (bindingResult.hasErrors()) {
            return "article_form";
        }

        Member member = this.memberService.getMember(principal.getName());
        this.articleService.create(articleDto.getSubject(), articleDto.getContent(), member, files);
        return "redirect:/";
    }


    @GetMapping("/article/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modify(@PathVariable("id") Integer id, Principal principal) {
        return "/article/article_form";
    }

    @PostMapping("/article/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String articleModify(@PathVariable("id") Integer id, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "/article/article_form";
        }

        Article article = this.articleService.getById(id);
        this.articleService.modify(article.getSubject(), article.getContent(), article);
        return "redirect:/";
    }

    @PostMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable("id") Integer id, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "article_list";
        }
        Article article = this.articleService.getById(id);
        this.articleService.delete(article);
        return "redirect:/";
    }

    @GetMapping("/admin/list")
    public String List(Model model) {
        List<Member> memberList = this.memberService.getAll();
        List<Notice> noticeList = this.noticeService.getAll();
        List<Article> articleList = this.articleService.getAll();
        List<Product> productList = this.productService.getAll();
        model.addAttribute("memberList", memberList);
        model.addAttribute("noticeList", noticeList);
        model.addAttribute("articleList", articleList);
        model.addAttribute("productList", productList);
        return "/admin/list";
    }
}
