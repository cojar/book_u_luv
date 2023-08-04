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
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(name = "nick_name", unique = true)
    private String nickName;

    @Column(name = "password")
    private String password;

    @Column(name = "tmp_password")
    private String tmpPassword;

    @Column(name = "address")
    private String address;

    @Column(name = "postal_num")
    private String postalNum;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "modify_date")
    private LocalDate modifyDate;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "img_file_path")
    private String imgFilePath;

    @Column(name = "img_file_name")
    private String imgFileName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "payment_info")
    private String paymentInfo;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column(name = "gender")
    private boolean gender;

    @Column(name = "mail_auth")
    private boolean mailAuth;

    @Column(name = "main_key")
    private Integer mainKey;
}
