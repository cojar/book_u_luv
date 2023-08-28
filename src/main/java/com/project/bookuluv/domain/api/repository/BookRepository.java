package com.project.bookuluv.domain.api.repository;

import com.project.bookuluv.domain.admin.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface BookRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(long myBookId);
}

