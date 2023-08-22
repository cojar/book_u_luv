package com.project.bookuluv.domain.cash;

import com.project.bookuluv.base.entity.BaseEntity;
import com.project.bookuluv.domain.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CashLog extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    private Member member;

    private long price;
}
