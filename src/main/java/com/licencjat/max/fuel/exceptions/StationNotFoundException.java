package com.licencjat.max.fuel.exceptions;

public class StationNotFoundException extends Exception {

    public StationNotFoundException(long id) {
        super("Station not found: " + id);
    }
}
