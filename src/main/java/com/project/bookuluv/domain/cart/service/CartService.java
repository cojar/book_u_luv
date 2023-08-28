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
    @Transactional
    public void addCart(Product newProduct, Member member, Integer amount) {

        Cart cart = cartRepository.findByMemberId(member.getId());

        if (cart == null) {
            cart = createCart(member);
            cartRepository.save(cart);
        }

        Optional<Product> _product = productRepository.findById(newProduct.getId());

        if (_product.isPresent()){
            Product product = _product.get();

            CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

            if (cartItem == null) {
                cartItem = cartItemService.createCartItem(cart, product, amount);
                cartItemRepository.save(cartItem);
            } else {

                CartItem update = cartItem;
                update.setCart(cartItem.getCart());
                update.setProduct(cartItem.getProduct());
                update.addCount(amount);
                update.setModifyDate(LocalDateTime.now());
                cartItemRepository.save(update);

            }

            cart.setCount(cart.getCount() * amount);

        } else {
            throw new DataNotFoundException("상품을 조회할 수 없습니다.");
        }
    }


    private Cart createCart(Member member) {
        Cart cart = Cart.builder()
                .member(member)
                .createDate(LocalDateTime.now())
                .count(0)
                .build();
        return this.cartRepository.save(cart);
    }



    public Cart getMemberCart(Long id) {
        return this.cartRepository.findByMemberId(id);
    }

    public List<CartItem> getAllMemberCart(Cart cart) {
        return this.cartItemRepository.findAllByCart(cart);
    }

    public CartItem findCartItemById(Long itemId) {
        Optional<CartItem> cartItem = cartItemRepository.findById(itemId);
        if (cartItem.isPresent()) {
            return cartItem.get();
        }
        throw new DataNotFoundException("장바구니에 담긴 상품이 없습니다.");
    }


    public Cart getCartByMemberId(Long id) {
        return this.cartRepository.findByMemberId(id);
    }
}