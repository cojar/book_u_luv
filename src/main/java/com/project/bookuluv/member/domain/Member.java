package com.project.bookuluv.member.domain;

import com.project.bookuluv.member.dto.MemberRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String userName;

    @Column(unique = true)
    private String nickName;

    private String password;

    private String tmpPassword;

    private String address;

    private String postalNum;

    private LocalDate createDate;

    private LocalDate modifyDate;

    private LocalDate birthDate;

    private LocalDateTime lastLogin;

    private String imgFilePath;

    private String imgFileName;

    private String phone;

    private String firstName;

    private String lastName;

    private String paymentInfo;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private boolean gender;

    private boolean mailAuth;

    private Integer mainKey;
}
