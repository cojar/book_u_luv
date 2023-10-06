package com.project.bookuluv.domain.review.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    @NotEmpty(message = "내용 입력은 필수항목입니다.")
    private String content;

    @Builder.Default
    private double rating = 5.0; // 별점 기본값 5.0으로 설정
}
