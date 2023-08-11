package com.project.bookuluv.app.comment.domain;

import com.project.bookuluv.app.article.domain.Product;
import com.project.bookuluv.base.entity.BaseEntity;
import com.project.bookuluv.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Comment extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

//    @Column(name = "create_date")
//    private LocalDateTime createDate;
//
//    @Column(name = "modify_date")
//    private LocalDateTime modifyDate;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Member register;

    @ManyToMany
    Set<Member> commentLike;









}
