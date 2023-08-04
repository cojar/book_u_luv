package com.project.bookuluv.app.article.domain;

import com.project.bookuluv.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Member register;

    private String subject;

    private String content;

    private LocalDate createDate;

    private LocalDate modifyDate;

    private int hit;
}
