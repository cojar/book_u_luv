package com.project.bookuluv;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.admin.service.ProductService;
import com.project.bookuluv.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final MemberService memberService;

    private final ProductService productService;

    @GetMapping("/")
    public String mainPage(Model model,
                           @RequestParam(value = "newBooksPage", defaultValue = "1") int newBooksPage,
                           @RequestParam(value = "bestsellersPage", defaultValue = "1") int bestsellersPage) {
        Pageable newBooksPageable = PageRequest.of(newBooksPage - 1, 5); // 5개씩 표시
        Pageable bestsellersPageable = PageRequest.of(bestsellersPage - 1, 5); // 5개씩 표시

        Page<Product> newBooks = productService.getNewBooksProductWithPagination(newBooksPageable);
        Page<Product> bestsellers = productService.getBestsellersProductWithPagination(bestsellersPageable);

        model.addAttribute("newBooks", newBooks);
        model.addAttribute("bestsellers", bestsellers);

        return "main";
    }
}
