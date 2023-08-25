package com.project.bookuluv.domain.cartItem.domain;

import com.project.bookuluv.base.entity.BaseEntity;
import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.cart.domain.Cart;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CartItem extends BaseEntity {

    @ManyToOne
    private Product product;

    @ManyToOne
    private Cart cart;

    private int count;

    public void addCount(int count) {
        this.count += count;
    }
}