package com.chi.bnbserv.exception;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String message) {
        super(String.format(message));
    }
}
