package com.project.bookuluv.app.article.service;

import com.project.bookuluv.app.article.dto.ArticleDto;
import com.project.bookuluv.app.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<ArticleDto> getAll() {
        return this.articleRepository.findAll();
    }
}
