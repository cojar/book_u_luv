package com.project.bookuluv.app.article.repository;

import com.project.bookuluv.app.article.dto.ArticleDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<ArticleDto, Long> {
}
