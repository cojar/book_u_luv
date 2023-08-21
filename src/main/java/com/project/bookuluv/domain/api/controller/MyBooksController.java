package com.project.bookuluv.domain.api.controller;

import com.project.bookuluv.domain.api.domain.Ebook;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class MyBooksController {

    private List<Ebook> bookList = new ArrayList<>();

    @PostMapping("/create")
    public ResponseEntity<String> writeReview(Authentication authentication) {
        return ResponseEntity.ok().body(authentication.getName() + "님의 리뷰 등록이 완료되었습니다.");
    }

    @GetMapping("")
    public ResponseEntity<List<Ebook>> myBookList(Authentication authentication) {
        return ResponseEntity.ok().body(bookList);
    }

    //@GetMapping("/{myBookId}")
    //public ResponseEntity<String> myBookDetail(@Value("id") Integer id) {
    //  return;
    //}


}
