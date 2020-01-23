package com.licencjat.max.paliwa.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserManager {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public User save(User u) {
        return userRepository.save(u);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void initUsers() {
//        userRepository.deleteAll();
//        save(new User(1L, "user", "user_email", passwordEncoder.encode("pass"),
//                "Name", "Surname", "USER", "xd"));
//        save(new User(1L, "admin", "user_email", passwordEncoder.encode("admin"),
//                "Name", "Surname", "ADMIN", "xd"));
//    }
}
