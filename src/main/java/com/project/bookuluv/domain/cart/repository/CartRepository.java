package com.project.bookuluv.domain.cart.repository;

import com.project.bookuluv.domain.cart.domain.Cart;
import com.project.bookuluv.domain.cartItem.domain.CartItem;
import com.project.bookuluv.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findCartByMemberId(Long id);

    Cart findByMemberId(Long id);

    List<CartItem> findAllMemberCart(Cart cart);
}
