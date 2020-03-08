package com.licencjat.max.paliwa.security.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserManager {

    private static final Logger log = LoggerFactory.getLogger(UserRegisterController.class);
    private UserRepository userRepository;

    @Autowired
    public UserManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public void saveAll(List<User> users) {
        userRepository.saveAll(users);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public void save(User u) {
        userRepository.save(u);
        log.info("Saved user: " + u.toString());
    }

}
