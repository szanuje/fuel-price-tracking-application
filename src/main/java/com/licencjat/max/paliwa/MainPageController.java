package com.licencjat.max.paliwa;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainPageController {

    @GetMapping
    public String homePageGet() {
        return "index";
    }

    @PostMapping
    public String homePagePost() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPageGet() {
        return "login";
    }

    @PostMapping("/login")
    public String loginPagePost() {
        return "login";
    }

    @GetMapping("/test")
    public String testPageGet() {
        return "test";
    }
}
