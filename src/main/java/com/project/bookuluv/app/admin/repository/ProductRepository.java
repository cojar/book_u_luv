package com.project.bookuluv.app.admin.repository;


import com.project.bookuluv.app.admin.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
