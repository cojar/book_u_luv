package com.example.demo.web.repository;

import com.example.demo.web.domain.article;
import com.example.demo.web.dto.articleDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface articleRepository extends JpaRepository<articleDto, Long> {
}
