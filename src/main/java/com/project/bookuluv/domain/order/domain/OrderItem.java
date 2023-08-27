package com.project.bookuluv.domain.order.domain;

import com.project.bookuluv.base.entity.BaseEntity;
import com.project.bookuluv.domain.admin.domain.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    private Order order;

    private LocalDateTime payDate;

    @ManyToOne
    private Product product;

    private Long payPrice;

    private boolean isPaid;
}