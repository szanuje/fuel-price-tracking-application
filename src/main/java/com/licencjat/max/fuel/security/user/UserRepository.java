package com.licencjat.max.fuel.security.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String login);

    Optional<User> findByEmail(String login);
}