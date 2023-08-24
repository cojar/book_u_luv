package com.project.bookuluv.domain.admin.controller;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.admin.dto.ProductDto;
import com.project.bookuluv.domain.admin.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/domestic/list")
    public String domesticList(Model model, Pageable pageable) {
        Page<Product> domestic = productService.getDomestic(pageable);
        model.addAttribute("domestic", domestic);
        return "product/domestic_list";
    }

    @GetMapping("/foreign/list")
    public String foreignList(Model model, Pageable pageable) {
        Page<Product> foreigner = productService.getForeign(pageable);
        model.addAttribute("foreigner", foreigner);
        return "product/foreign_list";
    }

    @GetMapping(value = "/domestic/detail/{id}")
    private String domesticDetail(Model model, @PathVariable("id") Long id) {
        Product products = this.productService.findById(id);
        model.addAttribute("products", products);
        return "product/detail";
    }

    @GetMapping(value = "/foreign/detail/{id}")
    private String foreignDetail(Model model, @PathVariable("id") Long id) {
        Product products = this.productService.findById(id);
        model.addAttribute("products", products);
        return "product/detail";
    }
}
