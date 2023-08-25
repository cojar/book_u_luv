package com.project.bookuluv.domain.admin.domain;

import com.project.bookuluv.base.entity.BaseEntity;
import com.project.bookuluv.domain.cartItem.domain.CartItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "link")
    private String link; // 책 링크(URL)

    @Column(name = "author")
    private String author;

    @Column(name = "pub_date")
    private String pubDate;

    @Column(name = "description")
    private String description;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "isbn13")
    private String isbn13;

    @Column(name = "price_standard")
    private Long priceStandard;

    @Column(name = "price_sales")
    private Long priceSales;

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

    @Column(name = "category_name")
    private String categoryName;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<CartItem> cartItemList;

}
