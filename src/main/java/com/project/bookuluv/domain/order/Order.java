package com.project.bookuluv.domain.order;

import com.project.bookuluv.base.entity.BaseEntity;
import com.project.bookuluv.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_order")
public class Order extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    private Member buyer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<OrderItem> orderItemList;

    private String name;

    private boolean isPaid;

    private boolean isCanceled;

    private boolean isRefunded;
}
