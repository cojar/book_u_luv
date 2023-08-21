package com.project.bookuluv.domain.api.domain;

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
public class Ebook extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "pub_date")
    private LocalDate pubDate;

    @Column(name = "description")
    private String description;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "price_standard")
    private int priceStandard;

    @Column(name = "cover_img")
    private String coverImg; // 표지 URL

    @Column(name = "publisher")
    private String publisher; // 출판사

    @Column(name = "adult")
    private boolean adult;

    @Column(name = "customer_review_rank")
    private int customerReviewRank;

}

