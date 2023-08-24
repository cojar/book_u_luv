package com.project.bookuluv.domain.order.service;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.member.domain.Member;
import com.project.bookuluv.domain.order.domain.Order;
import com.project.bookuluv.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order getByBuyerAndProductId(String userName, Long productId) {
        return orderRepository.findByBuyerUserNameAndProductId(userName, productId);
    }


    public Order createOrder(Member buyer, Product product) {

        Order order = Order.builder()
                .buyer(buyer)
                .product(product)
                .name(product.getTitle())
                .createDate(LocalDateTime.now())// 주문 이름은 제품의 제목으로 설정 (예시)
                .isPaid(false)
                .isCanceled(false)
                .isRefunded(false)
                .build();

        return orderRepository.save(order);
    }

    public Order getByBuyerAndProduct(Member member, Product product) {
        return this.orderRepository.findByBuyerAndProduct(member, product);
    }
}
