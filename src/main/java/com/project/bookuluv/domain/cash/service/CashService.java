package com.project.bookuluv.domain.cash.service;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.cash.domain.CashLog;
import com.project.bookuluv.domain.cash.repository.CashRepository;
import com.project.bookuluv.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashService {

    private final CashRepository cashRepository;

    public void addCashLog(Member member, String lastName, String firstName, Product product, String title, Integer price) {
        CashLog cashLog = CashLog.builder()
                .member(member)
                .lastName(lastName)
                .firstName(firstName)
                .product(product)
                .productTitle(title)
                .price(price)
                .build();

        this.cashRepository.save(cashLog);
    }
}
