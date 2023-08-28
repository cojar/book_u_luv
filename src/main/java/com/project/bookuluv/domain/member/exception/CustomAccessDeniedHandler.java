package com.project.bookuluv.domain.member.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 여기서 커스텀한 에러 페이지로 리다이렉트 또는 에러 메시지를 처리할 수 있음
        response.sendRedirect("/error_page"); // 예시: 에러 페이지로 리다이렉트
    }
}