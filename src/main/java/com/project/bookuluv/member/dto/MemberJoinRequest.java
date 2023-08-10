package com.project.bookuluv.member.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Component
public class MemberJoinRequest {
    @NotEmpty(message = "사용자ID는 필수항목입니다.")
    @Email
    private String userName;

   @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password1;

   @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String password2;

    @Size(min = 3, max = 25)
    private String nickName;

   @NotEmpty(message = "우편번호는 필수항목입니다.")
    private String postalNum;

   @NotEmpty(message = "연락처는 필수항목입니다.")
    private String phone;

   @NotEmpty(message = "이름은 필수항목입니다.")
    private String firstName;

   @NotEmpty(message = "성은 필수항목입니다.")
    private String lastName;

    @NotNull(message = "성별은 필수항목입니다.")
    private boolean gender;

    private LocalDate birthDate;

    private Integer mailKey = null;

    private Integer genMailKey;

    private boolean mailAuth;

    private String roadAddress;
    private String jibunAddress;
    private String detailAddress;
    private String extraAddress;
}
