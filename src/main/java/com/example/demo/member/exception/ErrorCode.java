package com.example.demo.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USERNAME_DUPLICATED(HttpStatus.CONFLICT, ""), USERNAME_NOT_FOUND(HttpStatus.CONFLICT, ""),
    INVALID_PASSWORD(HttpStatus.CONFLICT, "");

    private HttpStatus httpStatus;

    private String message;
}
