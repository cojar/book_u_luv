package com.project.bookuluv.domain.api.service;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.api.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository myBookRepository;
    public List<Product> findAll() {
        return myBookRepository.findAll();
    }

    public Product findByIdAndOwnerId(long myBookId) {
        return myBookRepository.findById(myBookId).orElseThrow(RuntimeException::new);
    }
}
