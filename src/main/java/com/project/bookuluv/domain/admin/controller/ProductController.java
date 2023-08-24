package com.project.bookuluv.domain.admin.controller;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.admin.dto.ProductDto;
import com.project.bookuluv.domain.admin.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

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
    private String domesticDetail(Model model, @PathVariable("id") Integer id) {
        Product products = this.productService.getById(id);
        model.addAttribute("products", products);
        return "product/detail";
    }

    @GetMapping(value = "/foreign/detail/{id}")
    private String foreignDetail(Model model, @PathVariable("id") Integer id) {
        Product products = this.productService.getById(id);
        model.addAttribute("products", products);
        return "product/detail";
    }
}
