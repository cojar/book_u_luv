package com.project.bookuluv.domain.article.repository;

import com.project.bookuluv.domain.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Article findById(Integer id);

}
