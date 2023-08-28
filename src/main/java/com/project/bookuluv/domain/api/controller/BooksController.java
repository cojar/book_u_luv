package com.project.bookuluv.domain.api.controller;

import com.project.bookuluv.Util.Ut;
import com.project.bookuluv.base.dto.RsData;
import com.project.bookuluv.base.rq.Rq;
import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.api.dto.BookResponse;
import com.project.bookuluv.domain.api.dto.BooksResponse;
import com.project.bookuluv.domain.api.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/books", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BooksController {
    final private BookService bookService;
    private final Rq rq;

    @GetMapping(value = "", consumes = ALL_VALUE)
    @Operation(summary = "로그인된 회원이 보유한 도서 목록", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<RsData<BooksResponse>> books() {
        List<Product> books = bookService.findAll();

        return Ut.sp.responseEntityOf(
                RsData.successOf(
                        BooksResponse.builder()
                                .books(books)
                                .build()
                )
        );
    }
    @GetMapping(value = "/{bookId}", consumes = ALL_VALUE)
    @Operation(summary = "로그인된 회원이 보유한 도서 단건", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<RsData<BookResponse>> book(@PathVariable long bookId) {
        Product book = bookService.findByIdAndOwnerId(bookId);
        return Ut.sp.responseEntityOf(
                RsData.successOf(
                        BookResponse.builder()
                                .book(book)
                                .build()
                )
        );
    }

}
