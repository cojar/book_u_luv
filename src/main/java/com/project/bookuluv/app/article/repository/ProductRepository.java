package com.project.bookuluv.app.article.repository;

import com.project.bookuluv.app.article.dto.ProductDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductDto, Long> {

}
