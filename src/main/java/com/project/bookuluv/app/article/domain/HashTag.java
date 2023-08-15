package com.project.bookuluv.app.article.domain;

import com.project.bookuluv.api.domain.ProductE;
import com.project.bookuluv.base.entity.BaseEntity;
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
public class HashTag extends BaseEntity {
    @ManyToOne
    private Keyword keyword;

    @ManyToOne
    private Product product;

    @ManyToOne
    private ProductE producte;


}
