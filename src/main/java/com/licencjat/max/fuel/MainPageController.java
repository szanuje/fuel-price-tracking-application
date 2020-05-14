package com.licencjat.max.fuel;

import com.licencjat.max.fuel.security.AuthenticationConstants;
import com.licencjat.max.fuel.security.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {

    @GetMapping(AuthenticationConstants.MAIN_PAGE_ENDPOINT)
    public String homePageGet(Model model) {
        // register form is on the main page and saves user to the database
        model.addAttribute("user", new User());
        return "index";
    }

    @GetMapping(AuthenticationConstants.TEST_URL_ENDPOINT)
    public String testPageGet() {
        return "test";
    }
}
