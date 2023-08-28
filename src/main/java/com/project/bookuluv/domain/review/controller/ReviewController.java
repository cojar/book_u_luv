package com.project.bookuluv.domain.review.controller;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.admin.service.ProductService;
import com.project.bookuluv.domain.member.domain.Member;
import com.project.bookuluv.domain.member.service.MemberService;
import com.project.bookuluv.domain.review.domain.Review;
import com.project.bookuluv.domain.review.dto.ReviewDto;
import com.project.bookuluv.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    private final ProductService productService;

    private final MemberService memberService;
    @PostMapping("/create/{id}")
    public String createReview(@PathVariable("id") Long id, @Valid ReviewDto reviewDto,
                               BindingResult bindingResult,
                               Principal principal,
                               Model model) {
        // 유효성 검사
        if (bindingResult.hasErrors()) {
            return "redirect:/product/detail/" + id + "?error=true";
        }

        // 현재 로그인한 사용자 정보 가져오기
        Member member = memberService.getUserByUserName(principal.getName());

        // 상품 정보 가져오기
        Product product = productService.getById(id);

        // ReviewDto와 상품 정보를 모델에 추가
        model.addAttribute("reviewDto", reviewDto);
        model.addAttribute("product", product);

        // 리뷰 생성 서비스 호출
        reviewService.create(reviewDto, member, product);
        productService.updateAverageRating(product);

        return "redirect:/product/detail/" + id + "?success=create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String reviewModify(@Valid ReviewDto reviewDto, @PathVariable("id") Long id,
                               BindingResult bindingResult, Principal principal,
                               Model model) {
        if (bindingResult.hasErrors()) {
            // 기존 리뷰 객체를 가져오기
            Review review = this.reviewService.getReview(id);

            // 리뷰 폼만 모델에 추가
            model.addAttribute("reviewDto", reviewDto);
            model.addAttribute("review", review); // 수정하려는 리뷰 객체도 추가

            // 리뷰 목록도 모델에 추가
            List<Review> reviews = this.reviewService.getReviewsForProduct(review.getProduct());
            model.addAttribute("reviews", reviews);

            return "product/detail";
        }
        Review review = this.reviewService.getReview(id);
        if (!review.getReviewRegister().getUserName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.reviewService.modify(review, reviewDto.getContent());
        return "redirect:/product/detail/" + review.getProduct().getId() + "#review_" + review.getId() + "?success=modify";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String reviewDelete(Principal principal, @PathVariable("id") Long id) {
        Review review = this.reviewService.getReview(id);
        if (!review.getReviewRegister().getUserName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.reviewService.delete(review);
        return "redirect:/product/detail/" + review.getProduct().getId() + "#review_" + review.getId() + "?success=delete";
    }
}
