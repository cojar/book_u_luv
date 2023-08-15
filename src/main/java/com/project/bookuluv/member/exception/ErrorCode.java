package com.project.bookuluv.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USERNAME_DUPLICATED(HttpStatus.CONFLICT, ""), USERNAME_NOT_FOUND(HttpStatus.CONFLICT, ""),
    INVALID_PASSWORD(HttpStatus.CONFLICT, ""), DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "");

    private HttpStatus httpStatus;

    private String message;
}
