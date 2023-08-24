package com.project.bookuluv.domain.admin.repository;


import com.project.bookuluv.domain.admin.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    long countByIsbn(String isbn);



    Page<Product> findByMallType(String book, Pageable pageable);
}
