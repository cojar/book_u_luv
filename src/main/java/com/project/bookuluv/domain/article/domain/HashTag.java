package com.project.bookuluv.domain.article.domain;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class HashTag extends BaseEntity {
    @ManyToOne
    private Keyword keyword;

    @ManyToOne
    private Product product;


}
