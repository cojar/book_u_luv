package com.project.bookuluv.app.admin.dto;

import com.project.bookuluv.member.domain.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class NoticeDto {

    private String subject;

    private String content;

    private Member register;

    private int hit;

}
