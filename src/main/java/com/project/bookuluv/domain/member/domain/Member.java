package com.project.bookuluv.domain.member.domain;

import com.project.bookuluv.base.entity.BaseEntity;
import com.project.bookuluv.domain.admin.domain.Notice;
import com.project.bookuluv.domain.member.dto.MemberRole;
import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class Member extends BaseEntity {

    @Column(name = "user_name", unique = true)
    private String userName; // 사용자 계정이름(email타입, 가입 시 인증필수)

    @Column(name = "password")
    private String password; // 사용자 비밀번호

    @Column(name = "tmp_password")
    private String tmpPassword; // 사용자 임시비밀번호(비밀번호 찾기 시에)

    @Column(name = "nick_name")
    private String nickName; // 사용자 닉네임

    @Column(name = "first_name")
    private String firstName; // 사용자 본명 중 이름

    @Column(name = "last_name")
    private String lastName; // 사용자 본명 중 성

    @Column(name = "phone")
    private String phone; // 사용자 연락처

    @Column(name = "socialEmail")
    private String socialEmail;

    @Column(name = "mail_auth")
    private boolean mailAuth; // 이메일 인증 여부(true : 인증함 / false : 인증하지 않음)

    @Column(name = "mail_key")
    private int mailKey; // 이메일 인증 시 사용자가 입력한 인증 키 번호

    @Column(name = "birth_date")
    private LocalDate birthDate; // 사용자 생년월일

    @Column(name = "last_login")
    private LocalDateTime lastLogin; // 사용자 마지막 로그인 일시

    @Column(name = "img_file_path")
    private String imgFilePath; // 사용자 프로필 이미지 파일 경로

    @Column(name = "img_file_name")
    private String imgFileName; // 사용자 프로필 이미지 파일 이름

    @Column(name = "gender")
    private Boolean gender; // 사용자 성별 (true : 남자 / false : 여자)

    @Column(name = "road_address")
    private String roadAddress; // 사용자 도로명 주소(kakao API)

    @Column(name = "jibun_address")
    private String jibunAddress; // 사용자 지번 주소(kakao API)

    @Column(name = "detail_address")
    private String detailAddress; // 사용자 상세 주소(kakao API)

    @Column(name = "extra_address")
    private String extraAddress; // 사용자 기타 주소(kakao API)

    @Column(name = "postal_num")
    private String postalNum; // 사용자 우편번호(kakao API)

    // 소셜로그인 기능을 위한 프로바이더와 프로바이더_id 칼럼 추가
    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "payment_info")
    private String paymentInfo; // 사용자 결제정보(임시)

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private MemberRole role; // 사용자 권한(Admin / User ..ETC)

    @Column(name = "is_active")
    private boolean isActive; // soft Delete를 처리하기 위한 칼럼. (회원 활성 여부)

    public void deactivate() {
        this.isActive = false;
    }

    @OneToMany(mappedBy = "register", cascade = CascadeType.REMOVE)
    private List<Notice> noticeList;


}
