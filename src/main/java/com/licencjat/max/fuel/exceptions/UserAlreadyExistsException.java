package com.licencjat.max.fuel.exceptions;

public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException(String username) {
        super("User already exists: " + username);
    }
}
