package com.project.bookuluv.domain.cartItem.repository;

import com.project.bookuluv.domain.cart.domain.Cart;
import com.project.bookuluv.domain.cartItem.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndProductId(Long cartId, Long productId);

    List<CartItem> findAllByCart(Cart cart);

}