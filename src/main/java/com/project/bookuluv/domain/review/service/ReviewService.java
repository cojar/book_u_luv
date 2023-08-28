package com.project.bookuluv.domain.review.service;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.admin.service.ProductService;
import com.project.bookuluv.domain.member.domain.Member;
import com.project.bookuluv.domain.member.exception.DataNotFoundException;
import com.project.bookuluv.domain.review.domain.Review;
import com.project.bookuluv.domain.review.dto.ReviewDto;
import com.project.bookuluv.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    private final ProductService productService;
    public List<Review> getList () {
        return this.reviewRepository.findAll();
    }

    public Review getReview(Long id) {
        Optional<Review> review = this.reviewRepository.findById(id);
        if (review.isPresent()) {
            return review.get();
        } else {
            throw new DataNotFoundException("review not found"); // 예외처리로 에러(DataNotFoundException)를 표시
        }
    }

    public List<Review> getReviewsByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    public Review create(ReviewDto reviewDto, Member member, Product product) {
        Review review = Review.builder()
                .content(reviewDto.getContent())
                .rating(reviewDto.getRating())
                .product(product)
                .reviewRegister(member)
                .createDate(LocalDateTime.now())
                .build();
        Review savedReview = reviewRepository.save(review);

        // 제품의 평균 별점 갱신
        productService.updateAverageRating(product);

        return savedReview;
    }

    public Review findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    public List<Review> getReviewsForProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    public void modify(Review review, String content) {
        review.setContent(content);
        review.setModifyDate(LocalDateTime.now());
        this.reviewRepository.save(review);
    }

    public void delete(Review review) {
        this.reviewRepository.delete(review);
    }
}
