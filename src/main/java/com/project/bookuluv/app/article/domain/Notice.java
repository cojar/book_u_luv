package com.project.bookuluv.app.article.domain;

import com.project.bookuluv.base.entity.BaseEntity;
import com.project.bookuluv.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Notice extends BaseEntity {

    @Column(name = "subject")
    private String subject;

    @Column(name = "content")
    private String content;

   // @Column(name = "hit")
  //  private int hit;

    @ManyToOne
    private Member register;


}
