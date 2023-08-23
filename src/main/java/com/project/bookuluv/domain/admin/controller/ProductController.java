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

    // 기존 국내도서 리스팅하여 불러오는 컨트롤러 메서드
//    @GetMapping("/domestic/list")
//    public String domesticList(Model model, Pageable pageable) {
//        Page<Product> domestic = productService.getDomestic(pageable);
//        model.addAttribute("domestic", domestic);
//        return "product/domestic_list";
//    }

    // SBB에서 개조한 국내도서 리스팅하여 불러오는 컨트롤러 메서드
    @GetMapping("/domestic/list")
    public String domesticList(Model model,
                       @RequestParam(value = "page", defaultValue = "1") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) { // url에 page내용이 없을땐 0값을 기본값으로 설정해라.
        if (page == 0) { // 페이지넘버가 0일때 page=1로 리디렉션 하라는 if문
            return "redirect:/product/domestic/list?page=1";
        }
        Page<Product> domestic = this.productService.getDomestic(page, kw);
        model.addAttribute("domestic", domestic);
        model.addAttribute("kw", kw);
        return "product/domestic_list";
    }


    @GetMapping("/foreign/list")
    public String foreignList(Model model, Pageable pageable) {
        Page<Product> foreigner = productService.getForeign(pageable);
        model.addAttribute("foreigner", foreigner);
        return "product/foreign_list";
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
