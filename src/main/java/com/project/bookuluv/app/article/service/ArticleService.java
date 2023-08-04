package com.project.bookuluv.app.article.service;

import com.project.bookuluv.app.article.domain.Article;
import com.project.bookuluv.app.article.repository.ArticleRepository;

import com.project.bookuluv.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<Article> getAll() {
        return this.articleRepository.findAll();
    }

    public Article getById(Integer id) {
        return this.articleRepository.findById(id);
    }

    public void create(String subject, String content, Member member) {
        Article article = new Article();
        article.setSubject(subject);
        article.setContent(content);
        article.setCreateDate(LocalDate.now());
        article.setRegister(member);
        this.articleRepository.save(article);

    }

    public void modify(String subject, String content, Article article) {
        article.setSubject(subject);
        article.setContent(content);
        article.setModifyDate(LocalDate.now());
        this.articleRepository.save(article);
    }

    public void delete(Article article) {
        this.articleRepository.delete(article);
    }
}
