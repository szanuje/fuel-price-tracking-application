package com.licencjat.max.paliwa.security;

import com.licencjat.max.paliwa.security.user.User;
import com.licencjat.max.paliwa.security.user.UserManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    UserManager userManager;
    PasswordEncoder passwordEncoder;

    public RegisterController(UserManager userManager, PasswordEncoder passwordEncoder) {
        this.userManager = userManager;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(AuthenticationConstants.REGISTER_POST_URL_ENDPOINT)
    public String addUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        if (userManager.findByUsername(user.getUsername()) != null) {
            redirectAttributes.addAttribute("register", "false");
            return "redirect:" + AuthenticationConstants.MAIN_PAGE_URL;
        }
        user.setRoles("USER");
        user.setAuthorities(AuthenticationConstants.userRoleAuthoritiesAsString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userManager.save(user);
        return "redirect:" + AuthenticationConstants.MAIN_PAGE_URL;
    }
}
