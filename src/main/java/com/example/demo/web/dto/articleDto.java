package com.example.demo.web.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
public class articleDto {
    @Entity
    @Getter
    @Setter
    public class article {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String register;

        private String subject;

        private String content;

        private LocalDate createDate;

        private LocalDate motifyDate;

        private int hit;
    }

}
