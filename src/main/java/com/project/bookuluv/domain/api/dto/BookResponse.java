package com.project.bookuluv.domain.api.dto;

import com.project.bookuluv.domain.admin.domain.Product;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookResponse {
    private Product book;
}

