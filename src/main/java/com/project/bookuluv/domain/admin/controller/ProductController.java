package com.project.bookuluv.domain.admin.controller;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.admin.dto.ProductDto;
import com.project.bookuluv.domain.admin.service.ProductService;
import com.project.bookuluv.domain.article.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchBooks(Model model, @RequestParam String query) {
        List<ProductDto> results = productService.searchBooks(query);
        model.addAttribute("results", results);
        return "searchBooks";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Product> productList = productService.getAll();
        model.addAttribute("productList", productList);
        return "/product/list";
    }

    @GetMapping(value = "/detail/{id}")
    private String detail(Model model, @PathVariable("id") Integer id) {
        Product products = this.productService.getById(id);
        model.addAttribute("products", products);
        return "article/detail";
    }
}
