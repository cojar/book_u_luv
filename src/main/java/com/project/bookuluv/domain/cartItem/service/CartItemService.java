package com.project.bookuluv.domain.cartItem.service;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.cart.domain.Cart;
import com.project.bookuluv.domain.cartItem.domain.CartItem;
import com.project.bookuluv.domain.cartItem.repository.CartItemRepository;
import com.project.bookuluv.domain.member.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItem createCartItem(Cart cart, Product product, int amount) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setCount(amount);
        return cartItem;
    }

    public void cartItemDelete(Long cartItemId) {
        Optional<CartItem> _cartItem = cartItemRepository.findById(cartItemId);

        if (_cartItem.isPresent()){
            CartItem cartItem = _cartItem.get();
            this.cartItemRepository.delete(cartItem);
        } else {
            throw new DataNotFoundException("장바구니 아이템을 찾지 못하였습니다.");
        }

    }

    public List<CartItem> getAll(Cart cart) {
        return this.cartItemRepository.findAll();
    }
}