package com.project.bookuluv;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MainController {

    @GetMapping("/")
    public String root () {
        return "main"; // ROOT로 접근했을 때 페이지가 해당 주소로 리다이렉트 되게끔 리턴.
    }

    @GetMapping("/main")
    public String mainPage() {
        return "main_page";
    }
}
