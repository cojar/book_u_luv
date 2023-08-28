package com.project.bookuluv.domain.order.domain;

import com.project.bookuluv.base.entity.BaseEntity;
import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_order")
public class Order extends BaseEntity {

    @ManyToOne(fetch = LAZY, cascade = CascadeType.REMOVE) // CascadeType.REMOVE 추가
    private Member buyer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderItem> orderItemList;

    @ManyToOne
    private Product product;

    private String name;

    private boolean isPaid;

    private boolean isCanceled;

    private boolean isRefunded;
}