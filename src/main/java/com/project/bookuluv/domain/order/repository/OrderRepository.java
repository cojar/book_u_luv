package com.project.bookuluv.domain.order.repository;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.member.domain.Member;
import com.project.bookuluv.domain.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByBuyerUserName(String userName);

    Order findByBuyerUserNameAndProductId(String userName, Long productId);

    Order findByBuyerAndProduct(Member member, Product product);
}
