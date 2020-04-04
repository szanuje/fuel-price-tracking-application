package com.licencjat.max.paliwa;

import com.licencjat.max.paliwa.security.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class MainPageController {

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("test", "no-cache");
    }

    @GetMapping
    public String homePageGet(Model model) {
        // register form is on the main page and saves user to the database
        model.addAttribute("user", new User());
        return "index";
    }

    @GetMapping("/test")
    public String testPageGet() {
        return "test";
    }
}
