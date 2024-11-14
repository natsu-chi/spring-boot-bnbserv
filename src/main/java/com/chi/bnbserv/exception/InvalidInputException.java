package com.chi.bnbserv.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String inputType, String inputName, String description) {
        super(String.format("invalid input - type: %s, name: %s, description: %s", inputType, inputName, description));
    }
}
