package com.vlad.code.sample.exception;

public class InvalidEmailException extends Throwable {
    public InvalidEmailException(String errorMessage) {
        super(errorMessage);
    }
}
