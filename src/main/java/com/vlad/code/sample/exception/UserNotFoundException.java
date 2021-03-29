package com.vlad.code.sample.exception;

public class UserNotFoundException extends Throwable {

    public final static String DEFAULT_ERROR_MESSAGE = "User is not found";

    public UserNotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }
}
