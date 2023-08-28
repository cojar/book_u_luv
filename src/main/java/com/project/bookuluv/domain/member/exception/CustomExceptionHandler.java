package com.project.bookuluv.domain.member.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = AccessDeniedException.class)
    public String handleAccessDeniedException() {
        return "error_page"; // 잘못된 접근 시 error_page.html 리턴
    }
}