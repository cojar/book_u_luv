package com.project.bookuluv.member.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
public class CustomMember extends User {
    private Member member = new Member();
    // 일반 이메일 계정으로 가입한 유저의 정보를 표시할 때 연결해주는 곳
    private final Long id;
    private final String userName;
    private final String nickName;
    private final String firstname;
    private final String lastname;
    private final String phone;
    private final String email;
    private final LocalDateTime createDate;
    private final String imgFilePath;
    private final Boolean gender;
    private final LocalDate birthDate;
    private final String postalNum;
    private final String roadAddress;
    private final String jibunAddress;
    private final String detailAddress;
    private final String extraAddress;


    public CustomMember(String username, String password, Member member, Collection<? extends GrantedAuthority> authorities) {
        super(username,
                password,
                authorities);
        this.id = member.getId();
        this.userName = member.getUserName();
        this.nickName = member.getNickName();
        this.firstname = member.getFirstName();
        this.lastname = member.getLastName();
        this.phone = member.getPhone();
        this.email = member.getSocialEmail();
        this.birthDate = member.getBirthDate();
        this.imgFilePath = member.getImgFilePath();
        this.gender = member.getGender();
        this.roadAddress = member.getRoadAddress();
        this.jibunAddress = member.getJibunAddress();
        this.detailAddress = member.getDetailAddress();
        this.extraAddress = member.getExtraAddress();
        this.postalNum = member.getPostalNum();
        this.createDate = member.getCreateDate();
    }
}
