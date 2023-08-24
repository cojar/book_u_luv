package com.project.bookuluv.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Component
public class MemberUpdateRequest {
    @Email
    private String userName;

    @NotEmpty(message = "닉네임을 입력해주세요")
//    @Pattern(regexp = "(?=.*[a-zA-Z가-힣])[a-zA-Z가-힣\\d]{2,15}")
    private String nickName;

    @NotEmpty(message = "연락처는 필수항목입니다.")
    private String phone;

    @NotEmpty(message = "이름은 필수항목입니다.")
    private String firstName;

    @NotEmpty(message = "성은 필수항목입니다.")
    private String lastName;

    @NotNull(message = "성별은 필수항목입니다.")
    private Boolean gender;

    @NotNull(message = "생년월일은 필수항목입니다.")
    private LocalDate birthDate;

    @NotEmpty(message = "우편번호는 필수항목입니다.")
    private String postalNum;
    @NotEmpty(message = "도로명주소는 필수항목입니다.")
    private String roadAddress;
    @NotEmpty(message = "지번주소는 필수항목입니다.")
    private String jibunAddress;
    @NotEmpty(message = "상세주소는 필수항목입니다.")
    private String detailAddress;
    @NotEmpty(message = "참고주소는 필수항목입니다.")
    private String extraAddress;
}
