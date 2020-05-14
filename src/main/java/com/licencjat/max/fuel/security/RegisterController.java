package com.licencjat.max.fuel.security;

import com.licencjat.max.fuel.exceptions.UserAlreadyExistsException;
import com.licencjat.max.fuel.security.user.User;
import com.licencjat.max.fuel.security.user.UserManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegisterController {

    UserManager userManager;
    PasswordEncoder passwordEncoder;

    public RegisterController(UserManager userManager, PasswordEncoder passwordEncoder) {
        this.userManager = userManager;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(AuthenticationConstants.REGISTER_ENDPOINT)
    public String addUser(HttpServletRequest servletRequest, @ModelAttribute("user") User user) throws UserAlreadyExistsException {
        if (userManager.findByUsername(user.getUsername()).isEmpty()) {
            user.setRoles("USER");
            user.setAuthorities(AuthenticationConstants.userRoleAuthoritiesAsString());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userManager.save(user);
            return "redirect:" + servletRequest.getHeader("Referer");
        } else {
            throw new UserAlreadyExistsException(user.getUsername());
        }
    }
}
