package com.project.bookuluv.app.article.repository;

import com.project.bookuluv.app.article.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
