package com.example.demo.web.repository;

import com.example.demo.web.dto.productDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface productRepository extends JpaRepository<productDto, Long> {

}
