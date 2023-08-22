package com.project.bookuluv.domain.cart;

import com.project.bookuluv.domain.api.domain.Ebook;
import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.base.entity.BaseEntity;
import com.project.bookuluv.domain.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Cart extends BaseEntity {
    @ManyToOne
    private Member member;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Ebook ebook;
}
