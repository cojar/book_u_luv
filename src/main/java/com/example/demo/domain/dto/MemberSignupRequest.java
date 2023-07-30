package com.example.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberSignupRequest {
    private String userName;
    private String password;
}
