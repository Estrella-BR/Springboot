package com.ccsw.tutorial.common.exception;

import com.ccsw.tutorial.client.exception.ExistsClient;
import com.ccsw.tutorial.loan.exception.DateRangeExceded;
import com.ccsw.tutorial.loan.exception.EndDateBeforBeginDate;
import com.ccsw.tutorial.loan.exception.GameLoaned;
import com.ccsw.tutorial.loan.exception.LoanLimit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DateRangeExceded.class)
    ResponseEntity<String> handleDateRange(DateRangeExceded ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

    @ExceptionHandler(LoanLimit.class)
    ResponseEntity<String> handleLoanLimitRange(LoanLimit ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

    @ExceptionHandler(EndDateBeforBeginDate.class)
    ResponseEntity<String> handleEndDateBeforBeginDate(EndDateBeforBeginDate ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

    @ExceptionHandler(GameLoaned.class)
    ResponseEntity<String> handleGameLoaned(GameLoaned ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

    @ExceptionHandler(ExistsClient.class)
    ResponseEntity<String> ExistsClient(ExistsClient ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

}
