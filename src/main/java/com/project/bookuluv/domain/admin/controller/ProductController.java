package com.project.bookuluv.domain.admin.controller;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.admin.dto.ProductDto;
import com.project.bookuluv.domain.admin.repository.ProductRepository;
import com.project.bookuluv.domain.admin.service.ProductService;

import com.project.bookuluv.domain.member.domain.Member;
import com.project.bookuluv.domain.member.service.MemberService;
import com.project.bookuluv.domain.review.domain.Review;
import com.project.bookuluv.domain.review.dto.ReviewDto;
import com.project.bookuluv.domain.review.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    @Value("${custom.genFileDirPath}")
    private String genFileDirPath;

    @Value("${custom.resourcePath}")
    private String resourcePath;

    private final ProductService productService;

    private final MemberService memberService;

    private final ProductRepository productRepository;

    private final ADMController admController;

    private final ReviewService reviewService;

    @GetMapping("/list")
    public String productList(Model model,
                              @RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "kw", defaultValue = "") String kw,
                              @RequestParam(value = "type") String type) {
        if (page <= 0) {
            return "redirect:/product/list?type=" + type + "&page=1";
        }

        Page<Product> products = this.productService.getProducts(type, page, kw);
        model.addAttribute("products", products);
        model.addAttribute("type", type);
        model.addAttribute("kw", kw);

        // 접근한 파라미터가 book 혹은 foreign일 경우에만 제품 리스트페이지를 반환
        if ("book".equals(type) || "foreign".equals(type)) {
            return "product/product_list";
        } else {
            // 다른 타입의 경우 에러페이지 반환
            return "error_page";
        }
    }

    @GetMapping(value = "/detail/{id}")
    public String domesticDetail(Model model, @PathVariable("id") Long id, Principal principal) {
        Product product = productService.getById(id);

        if (principal != null) {
            Member member = this.memberService.getMember(principal.getName());
            if (member != null) {
                model.addAttribute("member", member);
                Long memberId = member.getId();
                model.addAttribute("memberId", memberId);
            }
        }

        List<Review> reviews = reviewService.getReviewsByProduct(product); // Fetch reviews for the product
        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews); // Add the reviews to the model
        model.addAttribute("reviewDto", new ReviewDto()); // ReviewDto 객체 초기화
        productService.updateAverageRating(product);
        return "product/detail";
    }


    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(ProductDto productDto) {
        if (!admController.userIsAdmin()) { // 모든 관리자권한 가능
            throw new AccessDeniedException("Access is denied");
        }
        return "product/product_form";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String articleCreate(@ModelAttribute("productDto") @Valid ProductDto productDto,
                                BindingResult bindingResult,
                                @RequestParam("file1") MultipartFile file1,
                                @RequestParam("file2") MultipartFile file2) throws IOException {
        if (!admController.userIsAdmin()) { // 모든 관리자권한 가능
            throw new AccessDeniedException("Access is denied");
        }
        if (bindingResult.hasErrors()) {
            return "product/product_form";
        }
        Product createdProduct = this.productService.create(productDto, file1, file2);
        Long productId = createdProduct.getId();
        return "redirect:/product/detail/" + productId + "?success=create";
    }

    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modifyForm(@PathVariable("id") Long id, Model model, HttpServletRequest request) throws URISyntaxException {
        if (!admController.userIsAdmin()) { // 모든 관리자권한 가능
            throw new AccessDeniedException("Access is denied");
        }
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        ProductDto productDto = new ProductDto(); // 새로운 ProductDto 객체 생성

        // Product 객체의 정보를 ProductDto에 설정
        productDto.setTitle(product.getTitle());
        productDto.setAuthor(product.getAuthor());
        productDto.setPublisher(product.getPublisher());
        productDto.setPubDate(product.getPubDate());
        productDto.setPriceStandard(product.getPriceStandard());
        productDto.setPriceSales(product.getPriceSales());
        productDto.setDescription(product.getDescription());
        productDto.setAdult(product.isAdult());
        productDto.setIsbn(product.getIsbn());
        productDto.setCategoryName(product.getCategoryName());
        // 이미지 파일과 PDF 파일의 경로를 설정
        productDto.setCoverImg(product.getCoverImg());
        productDto.setContentsPdf(product.getContentsPdf());
        productDto.setCoverImgName(product.getCoverImgName());
        productDto.setContentsPdfName(product.getContentsPdfName());

        String defaultImgUrl = "https://media.istockphoto.com/id/1286477689/vector/drag-and-drop-icon-cursor-pointer-computer-mouse-drag-line-icon-vector-on-isolated-white.jpg?s=612x612&w=0&k=20&c=yK04wER5n-Wfe29PWc2BKruoKUpgkcSEYlXWleqP1IU=";
        String currentDomain = this.productService.getRootDomain(request.getRequestURL().toString());

        // 이미지 경로 처리
        String preImgFile;
        if (product.getCoverImg() != null) {
            if (product.getCoverImg().startsWith("http")) {
                preImgFile = product.getCoverImg();
            } else {
                preImgFile = currentDomain + product.getCoverImg();
            }
        } else {
            preImgFile = defaultImgUrl;
        }

        // PDF 파일 경로 처리
        String prePdfFile;
        if (product.getContentsPdf() != null) {
            if (product.getContentsPdf().startsWith("http")) {
                prePdfFile = product.getContentsPdf();
            } else {
                prePdfFile = currentDomain + product.getContentsPdfName();
            }
        } else {
            prePdfFile = null;
        }

        model.addAttribute("productDto", productDto);
        model.addAttribute("preimgFile", preImgFile);
        model.addAttribute("prePdfFile", prePdfFile);

        return "product/product_form"; // 수정 폼 페이지로 이동
    }

    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modifyProduct(@ModelAttribute("productDto") @Valid ProductDto productDto,
                                BindingResult bindingResult,
                                @PathVariable("id") Long id,
                                @RequestParam("file1") MultipartFile file1,
                                @RequestParam("file2") MultipartFile file2) throws IOException {
        if (!admController.userIsAdmin()) { // 모든 관리자권한 가능
            throw new AccessDeniedException("Access is denied");
        }

        if (bindingResult.hasErrors()) {
            return "product_form";
        }
        Product modifiedProduct = this.productService.modify(productDto, id, file1, file2);
        Long productId = modifiedProduct.getId();
        return "redirect:/product/detail/" + productId + "?success=modify";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable("id") Long id) {
        if (!admController.userIsAdmin()) { // 모든 관리자권한 가능
            throw new AccessDeniedException("Access is denied");
        }
        this.productService.delete(id);
        return "redirect:/";
    }

    @PostMapping("/increase-hit")
    @ResponseBody
    public String increaseHitCount(@RequestParam Long id) {
        productService.incrementHitCount(id);
        return "success";
    }

}