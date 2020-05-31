package com.licencjat.max.fuel.security;

import com.licencjat.max.fuel.exceptions.UserAlreadyExistsException;
import com.licencjat.max.fuel.security.user.User;
import com.licencjat.max.fuel.security.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegisterController {

    UserManager userManager;
    PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterController(UserManager userManager,
                              PasswordEncoder passwordEncoder) {
        this.userManager = userManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(AuthenticationConstants.REGISTER_ENDPOINT)
    @ResponseStatus(HttpStatus.CREATED)
    public String addUser(
            HttpServletRequest servletRequest,
            @ModelAttribute("user") User user
    ) throws UserAlreadyExistsException {
        if (userManager.findByUsername(user.getUsername()).isEmpty() &&
                userManager.findByEmail(user.getEmail()).isEmpty()) {
            user.setRoles("USER");
            user.setAuthorities(
                    AuthenticationConstants.userRoleAuthoritiesAsString());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userManager.save(user);
            return "redirect:" + servletRequest.getHeader("Referer");
        } else {
            throw new UserAlreadyExistsException("User already exists.");
        }
    }
}
