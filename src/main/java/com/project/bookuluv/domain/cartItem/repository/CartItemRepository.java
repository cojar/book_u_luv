package com.project.bookuluv.domain.cartItem.repository;

import com.project.bookuluv.domain.cartItem.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findCartItemByCartIdAndProductId(Long id, Long id1);
}
