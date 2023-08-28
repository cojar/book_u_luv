package com.project.bookuluv.domain.admin.domain;

import com.project.bookuluv.base.entity.BaseEntity;
import com.project.bookuluv.domain.member.domain.Member;
import jakarta.persistence.*;
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
public class Notice extends BaseEntity {

    @Column(name = "subject")
    private String subject;

    @Column(name = "content")
    private String content;

    @Column(name = "hit", columnDefinition = "integer default 0", nullable = false)
    private int hit;

    @ManyToOne
    @JoinColumn(name = "notice_register_id")
    private Member noticeRegister; // 공지사항 작성자


}