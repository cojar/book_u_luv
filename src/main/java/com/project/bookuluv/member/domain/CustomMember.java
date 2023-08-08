package com.ll.spirits.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.Collection;
@Getter
@Setter
public class CustomUser extends User {
    private String nickname;
    private LocalDate birthDate;

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String nickname, LocalDate birthDate) {
        super(username,
                password,
                authorities);
        this.nickname = nickname;
        this.birthDate = birthDate;
    }
}
