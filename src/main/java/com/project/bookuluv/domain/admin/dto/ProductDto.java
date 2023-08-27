package com.project.bookuluv.domain.admin.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ProductDto {

    private String title;

    private String link; // 책 링크(URL)

    private String author;

    private String pubDate;

    private String description;

    private String isbn;

    private String isbn13;

    private Long priceStandard;

    private Long priceSales;

    private String mallType; // 대분류

    private String stockStatus;

    private String coverImg; // 표지 URL

    private String publisher; // 출판사

    private boolean adult;

    private int customerReviewRank;

    private int bestRank;

    private char status; // 책 상태(A~C, N , E)

    private String categoryName;
}