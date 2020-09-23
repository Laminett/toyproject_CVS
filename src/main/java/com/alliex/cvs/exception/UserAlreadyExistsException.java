package com.alliex.cvs.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String username) {
        super("User " + username + " is already exists.");
    }

}
