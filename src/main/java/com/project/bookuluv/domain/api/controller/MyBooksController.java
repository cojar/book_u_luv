package com.project.bookuluv.domain.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MyBooksController {


    @PostMapping("/create")
    public ResponseEntity<String> writeReview(Authentication authentication) {
        return ResponseEntity.ok().body(authentication.getName() + "님의 리뷰 등록이 완료되었습니다.");
    }

    //@GetMapping("/{myBookId}")
    //public ResponseEntity<String> myBookDetail(@Value("id") Integer id) {
    //  return;
    //}


}
