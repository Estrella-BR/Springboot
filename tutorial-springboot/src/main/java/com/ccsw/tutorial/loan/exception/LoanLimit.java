package com.ccsw.tutorial.loan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "El cliente no puede tener más de 2 préstamos")
public class LoanLimit extends RuntimeException {

    public LoanLimit(String message) {
        super(message);
    }

}
