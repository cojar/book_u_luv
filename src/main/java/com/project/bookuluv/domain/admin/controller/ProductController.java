package com.project.bookuluv.domain.admin.controller;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.admin.dto.ProductDto;
import com.project.bookuluv.domain.admin.service.ProductService;
import com.project.bookuluv.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    private final MemberService memberService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchBooks(Model model, @RequestParam String query) {
        List<ProductDto> results = productService.searchBooks(query);
        model.addAttribute("results", results);
        return "searchBooks";
    }

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

    @GetMapping(value = "/domestic/detail/{id}")
    public String domesticDetail(Model model, @PathVariable("id") Long id) {
        Product products = this.productService.findById(id);
        model.addAttribute("products", products);
        return "product/detail";
    }

    @GetMapping(value = "/foreign/detail/{id}")
    public String foreignDetail(Model model, @PathVariable("id") Long id) {
        Product products = this.productService.findById(id);
        model.addAttribute("products", products);
        return "product/detail";
    }















    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    private String create(ProductDto productDto) {
        return "product/product_form";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String articleCreate(@ModelAttribute("productDto") @Valid ProductDto productDto,
                                 BindingResult bindingResult,
                                 @RequestParam("file1") MultipartFile file1,
                                 @RequestParam("file2") MultipartFile file2) throws IOException {
        if (bindingResult.hasErrors()) {
            return "product/product_form";
        }

        this.productService.create(productDto, file1, file2);
        return "redirect:/";
    }


    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modifyForm(@Valid ProductDto productDto, @PathVariable("id") Long id, Model model) {
        return "product_form";
    }

    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modifyProduct(@ModelAttribute("productDto") @Valid ProductDto productDto,
                                BindingResult bindingResult,
                                @PathVariable("id") Long id,
                                @RequestParam("files") MultipartFile[] files) throws IOException {
        if (bindingResult.hasErrors()) {
            return "product_form";
        }

        productService.modify(productDto, id, files);
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable("id") Long id, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "redirect:/";
        }
        Product product = this.productService.findById(id);
        this.productService.delete(product);
        return "redirect:/";
    }

}
