package com.project.bookuluv;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.admin.service.ProductService;
import com.project.bookuluv.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final MemberService memberService;

    private final ProductService productService;

    @GetMapping("/")
    public String root(Model model, Principal principal) {
        List<Product> newBooks = this.productService.getNewBooksProduct();
        List<Product> bestsellers = this.productService.getBestsellersProduct();

        model.addAttribute("newBooks", newBooks); // 'books' 대신 'newBooks'로 변경
        model.addAttribute("bestsellers", bestsellers); // 'books' 대신 'bestsellers'로 변경

        return "main";
    }
}
