package com.project.bookuluv.app.article.domain;

import com.project.bookuluv.base.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Keyword extends BaseEntity {
    @Column(name = "keyword")
    private String keyword;
}
