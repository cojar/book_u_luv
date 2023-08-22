package com.project.bookuluv.domain.admin.controller;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.admin.dto.ProductDto;
import com.project.bookuluv.domain.admin.service.ProductService;
import lombok.RequiredArgsConstructor;
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
    public String domesticList(Model model) {
        List<ProductDto> domestic = productService.getDomestic();
        model.addAttribute("domestic", domestic);
        return "product/domestic_list";
    }

    @GetMapping("/foreign/list")
    public String foreignList(Model model) {
        List<ProductDto> foreign = productService.getForeign();
        model.addAttribute("foreign", foreign);
        return "product/foreign_list";
    }

    @GetMapping(value = "/detail/{id}")
    private String detail(Model model, @PathVariable("id") Integer id) {
        Product products = this.productService.getById(id);
        model.addAttribute("products", products);
        return "product/detail";
    }
}
