package com.project.bookuluv.app.article.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProductDto {

    private String title;

    private String link; // 책 링크(URL)

    private String author;

    private String pubDate;

    private String description;

    private String isbn;

    private String isbn13;

    private Long priceStandard;

    private String mallType; // 대분류

    private String stockStatus;

    private String coverImg; // 표지 URL

    private String publisher; // 출판사

    private boolean adult;

    private int customerReviewRank;

    private int bestRank;

    private char status; // 책 상태(A~C, N , E)

    private Long categoryId;

    private String categoryName;
}
