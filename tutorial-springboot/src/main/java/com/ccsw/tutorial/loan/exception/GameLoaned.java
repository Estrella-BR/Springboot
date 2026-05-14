package com.ccsw.tutorial.loan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "El ya está siendo prestado")
public class GameLoaned extends RuntimeException {

    public GameLoaned(String message) {
        super(message);
    }

}