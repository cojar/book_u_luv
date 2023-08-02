package com.project.bookuluv.api.controller;

import com.project.bookuluv.api.domain.productE;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/myBooks")
public class MyBooksController {

    private List<productE> bookList = new ArrayList<>();


    @PostMapping()
    public ResponseEntity<String> writeReview(Authentication authentication) {
        return ResponseEntity.ok().body(authentication.getName() + "님의 리뷰 등록이 완료되었습니다.");
    }

    @GetMapping("")
    public ResponseEntity<List<productE>> myBookList(Authentication authentication) {
        return ResponseEntity.ok().body(bookList);
    }

    //@GetMapping("/{myBookId}")
    //public ResponseEntity<String> myBookDetail(@Value("id") Integer id) {
    //  return;
    //}


}
