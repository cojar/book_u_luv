package com.project.bookuluv.domain.api.dto;

import com.project.bookuluv.domain.admin.domain.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class BooksResponse {
    private List<Product> books;
}

