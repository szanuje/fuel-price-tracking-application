package com.licencjat.max.fuel.exceptions;

public class StationAlreadyExistsException extends Exception {

    public StationAlreadyExistsException(String address) {
        super("Station already exists: " + address);
    }
}
