package com.licencjat.max.paliwa;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {

    @GetMapping("/home")
    public String homepage() {
        return "index";
    }

    @GetMapping("/test")
    public String testpage() {
        return "test";
    }
}
