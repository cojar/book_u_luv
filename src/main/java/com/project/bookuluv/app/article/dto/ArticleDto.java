package com.project.bookuluv.app.article.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ArticleDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String register;

    private String subject;

    private String content;

    private LocalDate createDate;

    private LocalDate modifyDate;

    private int hit;
}


