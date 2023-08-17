package com.project.bookuluv.cart;

import com.project.bookuluv.api.domain.ProductE;
import com.project.bookuluv.app.article.domain.Product;
import com.project.bookuluv.base.entity.BaseEntity;
import com.project.bookuluv.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart extends BaseEntity {
    @ManyToOne
    private Member member;

    @ManyToOne
    private Product product;

    @ManyToOne
    private ProductE producte;
}
