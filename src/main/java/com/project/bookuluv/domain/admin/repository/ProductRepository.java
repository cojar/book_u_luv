package com.project.bookuluv.domain.admin.repository;


import com.project.bookuluv.domain.admin.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    long countByIsbn(String isbn);

    Product getById(Integer id);
}
