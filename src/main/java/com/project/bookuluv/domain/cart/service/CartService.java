package com.project.bookuluv.domain.cart.service;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.admin.repository.ProductRepository;
import com.project.bookuluv.domain.cart.domain.Cart;
import com.project.bookuluv.domain.cart.repository.CartRepository;
import com.project.bookuluv.domain.cartItem.domain.CartItem;
import com.project.bookuluv.domain.cartItem.repository.CartItemRepository;
import com.project.bookuluv.domain.cartItem.service.CartItemService;
import com.project.bookuluv.domain.member.domain.Member;
import com.project.bookuluv.domain.member.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private final CartItemRepository cartItemRepository;

    private final CartItemService cartItemService;

    private final CartService cartService;

    @Transactional
    public void addCart(Product newProduct, Member member, int amount) {

        Cart cart = cartRepository.findCartByMemberId(member.getId());

        if (cart == null) {
            cart = cartService.createCart(member);
            cartRepository.save(cart);
        }

        Product product = productRepository.findByProductId(newProduct.getId());

        CartItem cartItem = cartItemRepository.findCartItemByCartIdAndProductId(cart.getId(), product.getId());

        if (cartItem == null) {
            cartItem = cartItemService.createCartItem(cart, product, amount);
            cartItemRepository.save(cartItem);
        } else {

            CartItem update = cartItem;
            update.setCart(cartItem.getCart());
            update.setProduct(cartItem.getProduct());
            update.addCount(amount);
            update.setModifyDate(LocalDateTime.now());
            cartItemRepository.save(cartItem);

        }

        cart.setCount(cart.getCount() * amount);
    }


    private Cart createCart(Member member) {
        Cart cart = Cart.builder()
                .member(member)
                .createDate(LocalDateTime.now())
                .count(0)
                .build();
        return this.cartRepository.save(cart);
    }

    public void cartItemDelete(Long cartItemId) {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        cartItem.ifPresentOrElse(
                item -> cartItemRepository.delete(item),
                () -> {
                    throw new DataNotFoundException("삭제할 장바구니 항목을 찾을 수 없습니다.");
                }
        );
    }

    public Cart getMemberCart(Long id) {
        return this.cartRepository.findByMemberId(id);
    }

    public List<CartItem> getAllMemberCart(Cart cart) {
        return this.cartRepository.findAllMemberCart(cart);
    }

    public List<CartItem> findAll(Cart cart) {
        return this.cartRepository.findAllMemberCart(cart);
    }

    public CartItem findCartItemById(Long itemId) {
        Optional<CartItem> cartItem = cartItemRepository.findById(itemId);
        if (cartItem.isPresent()) {
            return cartItem.get();
        }
        throw new DataNotFoundException("장바구니에 담긴 상품이 없습니다.");
    }


    public Cart getCartByMemberId(Long id) {
        return this.cartRepository.findCartByMemberId(id);
    }
}
