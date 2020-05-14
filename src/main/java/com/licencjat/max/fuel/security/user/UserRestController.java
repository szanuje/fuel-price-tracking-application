package com.licencjat.max.fuel.security.user;

import com.licencjat.max.fuel.security.AuthenticationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(AuthenticationConstants.ALL_USERS_ENDPOINT)
public class UserRestController {

    UserManager userManager;

    @Autowired
    public UserRestController(UserManager userManager) {
        this.userManager = userManager;
    }

    @GetMapping
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userManager.findAll().iterator().forEachRemaining(users::add);
        return users;
    }
}
