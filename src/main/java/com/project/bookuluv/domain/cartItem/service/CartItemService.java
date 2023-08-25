package com.project.bookuluv.domain.cartItem.service;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.cart.domain.Cart;
import com.project.bookuluv.domain.cartItem.domain.CartItem;

public class CartItemService {
    public static CartItem createCartItem(Cart cart, Product product, int amount) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setCount(amount);
        return cartItem;
    }
}
