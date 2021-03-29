package com.vlad.code.sample.exception;

public class DuplicateUserException extends Throwable {

    public DuplicateUserException(String errorMessage) {
        super(errorMessage);
    }
}
