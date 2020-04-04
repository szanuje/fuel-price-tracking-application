package com.licencjat.max.paliwa.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @RequestMapping(AuthenticationConstants.LOGIN_POST_URL_ENDPOINT)
    public String redirectFromLogin(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("login", "true");
        return "redirect:" + AuthenticationConstants.MAIN_PAGE_URL;
    }


}
