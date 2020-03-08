package com.licencjat.max.paliwa.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class UserRegisterController {

    private UserManager userManager;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserRegisterController(UserManager userManager, PasswordEncoder passwordEncoder) {
        this.userManager = userManager;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerPageGet(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping
    public String addUser(@ModelAttribute("user") User user) {
        user.setRoles("USER");
        user.setPermissions(Permissions.getUserRoleAuthorities());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userManager.save(user);
        return "index";
    }

}
