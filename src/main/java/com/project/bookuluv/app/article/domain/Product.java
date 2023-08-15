package com.project.bookuluv.app.article.domain;

import com.project.bookuluv.base.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Product extends BaseEntity {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "link")
    private String link; // 책 링크(URL)

    @Column(name = "author")
    private String author;

    @Column(name = "pub_date")
    private LocalDate pubDate;

    @Column(name = "description")
    private String description;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "isbn13")
    private String isbn13;

    @Column(name = "price_standard")
    private int priceStandard;

    @Column(name = "mall_type")
    private String mallType; // 대분류

    @Column(name = "stock_status")
    private String stockStatus;

    @Column(name = "cover_img")
    private String coverImg; // 표지 URL

    @Column(name = "publisher")
    private String publisher; // 출판사

    @Column(name = "adult")
    private boolean adult;

    @Column(name = "customer_review_rank")
    private int customerReviewRank;

    @Column(name = "best_rank")
    private int bestRank;

    @Column(name = "status")
    private char status; // 책 상태(A~C, N , E)

    @Column(name = "is_active")
    private boolean isActive;

}
