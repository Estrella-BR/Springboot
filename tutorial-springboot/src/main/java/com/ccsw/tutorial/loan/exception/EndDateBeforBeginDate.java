package com.ccsw.tutorial.loan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "La fecha de fin está antes que la de inicio")

public class EndDateBeforBeginDate extends RuntimeException {

    public EndDateBeforBeginDate(String message) {
        super(message);
    }

}
