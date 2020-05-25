package com.licencjat.max.fuel.exceptions.handlers;

import com.licencjat.max.fuel.security.AuthenticationConstants;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class GlobalErrorHandlingController implements ErrorController {

    @Override
    public String getErrorPath() {
        return AuthenticationConstants.ERROR_PAGE_ENDPOINT;
    }

    @RequestMapping(AuthenticationConstants.ERROR_PAGE_ENDPOINT)
    public String handleError(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if(request.getAttribute("javax.servlet.forward.request_uri").equals("/login")) {
            redirectAttributes.addAttribute("login", "true");
            return "redirect:";
        }
        return "error";
    }
}
