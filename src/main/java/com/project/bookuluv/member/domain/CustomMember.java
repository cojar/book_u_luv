package com.project.bookuluv.member.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.Collection;
@Getter
@Setter
public class CustomMember extends User {
    private String nickName;
    private LocalDate birthDate;

    public CustomMember(String username, String password, Collection<? extends GrantedAuthority> authorities, String nickName, LocalDate birthDate) {
        super(username,
                password,
                authorities);
        this.nickName = nickName;
        this.birthDate = birthDate;
    }
}
