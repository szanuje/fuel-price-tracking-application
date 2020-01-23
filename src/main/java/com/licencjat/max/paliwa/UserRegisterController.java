package com.licencjat.max.paliwa;

import com.licencjat.max.paliwa.security.user.User;
import com.licencjat.max.paliwa.security.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        System.out.println(user.toString());
        user.setRoles("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.toString());
        userManager.save(user);
        return "index";
    }

}
