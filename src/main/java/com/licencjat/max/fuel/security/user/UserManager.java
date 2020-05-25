package com.licencjat.max.fuel.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserManager {

    private UserRepository userRepository;

    @Autowired
    public UserManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public Optional<User> findByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User u) {
        return userRepository.save(u);
    }

    public Iterable<User> saveAll(List<User> users) {
        return userRepository.saveAll(users);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }
}
