package com.example.demo.web.service;

import com.example.demo.web.dto.articleDto;
import com.example.demo.web.repository.articleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class articleService {

    private final articleRepository articleRepository;

    public List<articleDto> getAll() {
        return this.articleRepository.findAll();
    }
}
