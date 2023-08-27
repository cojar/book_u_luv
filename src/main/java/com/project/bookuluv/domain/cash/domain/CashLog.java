package com.project.bookuluv.domain.cash.domain;

import com.project.bookuluv.base.entity.BaseEntity;
import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.member.domain.Member;
import com.project.bookuluv.domain.order.domain.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CashLog extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")  // 연관된 Product의 ID를 저장할 컬럼명
    private Product product;

    private String productTitle;

    private String lastName;

    private String firstName;

    private long price;


}