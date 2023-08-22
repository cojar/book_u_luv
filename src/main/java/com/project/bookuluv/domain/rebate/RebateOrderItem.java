package com.project.bookuluv.domain.rebate;

import com.project.bookuluv.base.entity.BaseEntity;
import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.order.Order;
import com.project.bookuluv.domain.order.OrderItem;
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
public class RebateOrderItem extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    private OrderItem orderItem;
    @ManyToOne(fetch = LAZY)
    private Order order;
    @ManyToOne(fetch = LAZY)
    private Product product;

    private int price; // 판매가

    private int payPrice; // 결제금액

    private boolean isPaid; // 결제여부

    private LocalDateTime payDate;
}
