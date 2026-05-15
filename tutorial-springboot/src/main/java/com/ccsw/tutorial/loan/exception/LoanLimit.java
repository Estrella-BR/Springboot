package com.ccsw.tutorial.loan.exception;

public class LoanLimit extends RuntimeException {

    public LoanLimit(String message) {
        super(message);
    }

}
