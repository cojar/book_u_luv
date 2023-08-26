package com.project.bookuluv.domain.admin.domain;

import com.project.bookuluv.base.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Product extends BaseEntity {

    @Column(name = "title")
    private String title; // 책 제목

    @Column(name = "link")
    private String link; // 책 링크(URL)

    @Column(name = "author")
    private String author; // 작가

    @Column(name = "pub_date")
    private String pubDate; // 출간일

    @Column(name = "description")
    private String description; // 제품 상세설명

    @Column(name = "isbn")
    private String isbn; // isbn 랜덤생성

    @Column(name = "isbn13")
    private String isbn13; // isbn13

    @Column(name = "price_standard")
    private Long priceStandard; // 정가

    @Column(name = "price_sales")
    private Long priceSales; // 판매가

    @Column(name = "mall_type")
    private String mallType; // 국내 / 국외 구분

    @Column(name = "cover_img")
    private String coverImg; // 책 표지 이미지파일 URL

    @Column(name = "contents_pdf")
    private String contentsPdf; // 책 내용 PDF파일 URL

    @Column(name = "cover_img_name")
    private String coverImgName; // 책 표지 이미지파일 이름

    @Column(name = "contents_pdf_name")
    private String contentsPdfName; // 책 내용 PDF파일 이름

    @Column(name = "publisher")
    private String publisher; // 출판사

    @Column(name = "adult")
    private boolean adult; // 청불 여부

    @Column(name = "status")
    private char status; // 책 상태(A~C, N , E)

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "hit", columnDefinition = "integer default 0", nullable = false)
    private int hit;

    // TODO : 별점과 좋아요 처리 해야 함.

}
