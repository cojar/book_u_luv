package com.project.bookuluv.domain.order.service;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.member.domain.Member;
import com.project.bookuluv.domain.order.domain.Order;
import com.project.bookuluv.domain.order.domain.OrderItem;
import com.project.bookuluv.domain.order.repository.OrderItemRepository;
import com.project.bookuluv.domain.order.repository.OrderRepository;
import com.project.bookuluv.domain.rebate.service.RebateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final RebateService rebateService;

    public Order getByBuyerAndProductId(String userName, Long productId) {
        return orderRepository.findByBuyerUserNameAndProductId(userName, productId);
    }


    @Transactional
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

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .isPaid(true)
                .payDate(LocalDateTime.now())
                .payPrice(product.getPriceSales())
                .build();

        orderItemRepository.save(orderItem);


        return orderRepository.save(order);
    }

}