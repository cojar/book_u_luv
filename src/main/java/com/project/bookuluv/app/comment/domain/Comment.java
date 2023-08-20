package com.project.bookuluv.app.comment.domain;

import com.project.bookuluv.api.domain.ProductE;
import com.project.bookuluv.app.admin.domain.Product;
import com.project.bookuluv.base.entity.BaseEntity;
import com.project.bookuluv.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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
@SuperBuilder
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
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "producte_id")
    private ProductE producte;
    @ManyToOne
    @JoinColumn(name = "register_id")
    private Member register;

}
