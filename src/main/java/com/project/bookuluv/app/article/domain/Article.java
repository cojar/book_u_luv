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
@SuperBuilder(toBuilder = true)
public class Article extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content")
    private String content;

//    @Column(name = "create_date")
//    private LocalDate createDate;
//
//    @Column(name = "modify_date")
//    private LocalDate modifyDate;

    @Column(name = "hit")
    private int hit;

    @ManyToOne
    private Member register;

    private List<String> filePaths;

    private List<String> fileNames;

}
