package com.ccsw.tutorial.loan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "El prestamo dura mas de 14 dias")
public class DateRangeExceded extends RuntimeException {

    public DateRangeExceded(String message) {
        super(message);
    }

}
