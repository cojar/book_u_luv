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
    // 일반 이메일 계정으로 가입한 유저의 정보를 표시할 때 연결해주는 곳
    private String nickName;
    private LocalDate birthDate;
    private String email;
    private LocalDateTime createDate;
    private String imgFilePath;
    Member member = new Member();

    public CustomMember(String username, String password, Collection<? extends GrantedAuthority> authorities, String nickName, LocalDate birthDate) {
        super(username,
                password,
                authorities);
        this.nickName = nickName;
        this.birthDate = birthDate;
        this.email = username;
        this.createDate = member.getCreateDate();
        this.imgFilePath = member.getImgFilePath();
    }
}
