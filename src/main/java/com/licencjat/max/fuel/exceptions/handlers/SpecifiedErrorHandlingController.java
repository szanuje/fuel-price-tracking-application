package com.licencjat.max.fuel.exceptions.handlers;

import com.licencjat.max.fuel.exceptions.EmptyPricesException;
import com.licencjat.max.fuel.exceptions.StationAlreadyExistsException;
import com.licencjat.max.fuel.exceptions.StationNotFoundException;
import com.licencjat.max.fuel.exceptions.UserAlreadyExistsException;
import com.licencjat.max.fuel.security.AuthenticationConstants;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class SpecifiedErrorHandlingController {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String userExistsHandler(HttpServletRequest servletRequest, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("register", "false");
        return "redirect:" + servletRequest.getHeader("Referer");
    }

    @ExceptionHandler(StationAlreadyExistsException.class)
    public String stationExistsHandler(HttpServletRequest servletRequest, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("stationExists", "true");
        return "redirect:" + servletRequest.getHeader("Referer");
    }

    @ExceptionHandler({StationNotFoundException.class, EmptyPricesException.class})
    @ResponseBody
    public String failureResponseHandler() {
        return "failure";
    }
}
