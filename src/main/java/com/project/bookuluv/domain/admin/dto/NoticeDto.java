package com.project.bookuluv.domain.admin.dto;

import com.project.bookuluv.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto {

    private String subject;

    private String content;

    private Member register;

    private int hit;

}
