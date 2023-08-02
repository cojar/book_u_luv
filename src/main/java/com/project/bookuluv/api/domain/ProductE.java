package com.project.bookuluv.api.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
public class ProductE {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String link; // 책 링크(URL)

    private String author;

    private LocalDate pubDate;

    private String description;

    private String isbn;

    private String isbn13;

    private int priceStandard;

    private String mallType; // 대분류

    private String stockStatus;

    private String coverImg; // 표지 URL

    private String publisher; // 출판사

    private boolean adult;

    private int customerReviewRank;

    private int bestRank;

    private char status; // 책 상태(A~C, N , E)

}
