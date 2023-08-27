package com.project.bookuluv.domain.review.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    @NotEmpty(message = "내용 입력은 필수항목입니다.")
    private String content;

    private double rating = 5.0; // 별점 기본값 5.0으로 설정
}
