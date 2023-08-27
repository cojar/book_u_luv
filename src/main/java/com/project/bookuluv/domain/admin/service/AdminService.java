package com.project.bookuluv.domain.admin.service;

import com.project.bookuluv.domain.member.domain.Member;
import com.project.bookuluv.domain.member.dto.MemberRole;
import com.project.bookuluv.domain.member.exception.AppException;
import com.project.bookuluv.domain.member.exception.ErrorCode;
import com.project.bookuluv.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final PasswordEncoder encoder;

    private final MemberRepository memberRepository;

    @Value("${custom.adminSignupKey}")
    private String adminSignupKey;

    public boolean isAdminSignupKeyValid(String inputKey) {
        return inputKey.equals(adminSignupKey);
    }

    public String createAdminMember(String userName,
                                    String password,
                                    String nickName,
                                    String roadAddress,
                                    String jibunAddress,
                                    String detailAddress,
                                    String extraAddress,
                                    String postalNum,
                                    String phone,
                                    String firstName,
                                    String lastName,
                                    Boolean gender,
                                    LocalDate birthDate,
                                    Integer mailKey,
                                    MemberRole role,
                                    LocalDateTime currentDate,
                                    boolean mailAuth,
                                    String adminKey) {

        // userName 중복체크
        memberRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED, userName + " 는 존재합니다.");
                });
        if (isAdminSignupKeyValid(String.valueOf(adminKey))) { // adminKey가 환경변수(adminSignupKey)와 맞으면
            // 관리자 저장
            Member member = Member.builder()
                    .userName(userName)
                    .password(encoder.encode(password))
                    .nickName(nickName)
                    .roadAddress(roadAddress)
                    .jibunAddress(jibunAddress)
                    .detailAddress(detailAddress)
                    .extraAddress(extraAddress)
                    .postalNum(postalNum)
                    .phone(phone)
                    .firstName(firstName)
                    .lastName(lastName)
                    .gender(gender)
                    .birthDate(birthDate)
                    .mailKey(mailKey)
                    .role(role) // 여기서 값은 SUPERADMIN
                    .mailAuth(mailAuth)
                    .createDate(LocalDateTime.now())
                    .isActive(true)
                    .build();

            this.memberRepository.save(member);
        } else {
            throw new IllegalArgumentException("Invalid admin key");
        }
        return "SUCCESS";
    }
}