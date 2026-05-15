package com.ccsw.tutorial.client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "El cliente ya existe")

public class ExistsClient extends RuntimeException {

    public ExistsClient(String message) {
        super(message);
    }

}