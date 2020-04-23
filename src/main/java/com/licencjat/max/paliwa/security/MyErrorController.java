package com.licencjat.max.paliwa.security;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return AuthenticationConstants.ERROR_PAGE_ENDPOINT;
    }

    @RequestMapping(AuthenticationConstants.ERROR_PAGE_ENDPOINT)
    public String handleError(HttpServletRequest request, RedirectAttributes redirectAttributes) {
//        String referer = request.getHeader("Referer");
//        if (referer.endsWith("/")) {
//            redirectAttributes.addAttribute("login", "false");
//            return "redirect:" + AuthenticationConstants.MAIN_PAGE_URL;
//        }
        return "error";
    }
}
