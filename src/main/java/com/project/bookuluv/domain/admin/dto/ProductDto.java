package com.project.bookuluv.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String title; // 책 제목

    private String link; // 책 링크(URL)

    private String author; // 작가

    private String pubDate; // 출판일

    private String description; // 상세설명

    private String isbn; // isbn

    private String isbn13; // isbn13

    private Long priceStandard; // 정가

    private Long priceSales; // 판매가

    private String mallType; // 국내 / 국외 구분

    private String coverImg; // 표지 URL

    private String contentsPdf; // PDF URL

    private String coverImgName; // 표지 이름

    private String contentsPdfName; // PDF 이름

    private String publisher; // 출판사

    private boolean adult; // 청불 여부

    private String categoryName; // 카테고리 : ex) 국내도서>예술/대중문화>예술/대중문화의 이해>미학/예술이론  select태그 option으로 프론트템플릿에서 지정하면 productService에서 등록할때 concat이나 += 혹은 append 등으로 한 줄로 이어붙여서 문자열로 가공하여 데이터베이스에 입력
}