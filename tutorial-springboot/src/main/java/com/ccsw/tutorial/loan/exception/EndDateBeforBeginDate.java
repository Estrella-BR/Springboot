package com.ccsw.tutorial.loan.exception;

public class EndDateBeforBeginDate extends RuntimeException {

    public EndDateBeforBeginDate(String message) {
        super(message);
    }

}
