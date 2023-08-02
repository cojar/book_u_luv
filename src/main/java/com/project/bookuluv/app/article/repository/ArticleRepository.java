package com.project.bookuluv.app.article.repository;

import com.project.bookuluv.app.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
