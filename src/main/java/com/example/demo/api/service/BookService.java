package com.example.demo.api.service;

import com.example.demo.api.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.print.Book;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;


}
