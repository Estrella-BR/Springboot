package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.LoanDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoanIT {
    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/loan";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<List<LoanDto>> responseType = new ParameterizedTypeReference<List<LoanDto>>() {
    };

    @Test
    public void findAllShouldReturnAllLoans() {

        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);

        assertNotNull(response);
        assertEquals(3, response.getBody().size());
    }

    public static final Long NEW_LOAN_ID = 4L;
    public static final Date NEW_LOAN_BEGINDATE = Date.from(Instant.parse("2025-08-15T23:00:00.000+00:00"));
    public static final Date NEW_LOAN_ENDNDATE = Date.from(Instant.parse("2025-08-19T23:00:00.000+00:00"));

    @Test
    public void saveWithoutIdShouldCreateNewLoan() {
        LoanDto dto = new LoanDto();
        dto.setBeginDate(NEW_LOAN_BEGINDATE);
        dto.setEndDate(NEW_LOAN_ENDNDATE);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);
        assertNotNull(response);
        assertEquals(4, response.getBody().size());

        LoanDto loanSearch = response.getBody().stream().filter(item -> item.getId().equals(NEW_LOAN_ID)).findFirst().orElse(null);
        assertNotNull(loanSearch);
        assertEquals(NEW_LOAN_BEGINDATE, loanSearch.getBeginDate());
    }

    public static final Long MODIFY_LOAN_ID = 3L;

    @Test
    public void modifyWithExistIdShouldModifyLoan() {

        LoanDto dto = new LoanDto();
        dto.setBeginDate(NEW_LOAN_BEGINDATE);
        dto.setEndDate(NEW_LOAN_ENDNDATE);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + MODIFY_LOAN_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);
        assertNotNull(response);
        assertEquals(3, response.getBody().size());

        LoanDto loanSearch = response.getBody().stream().filter(item -> item.getId().equals(MODIFY_LOAN_ID)).findFirst().orElse(null);
        assertNotNull(loanSearch);
        assertEquals(NEW_LOAN_BEGINDATE, loanSearch.getBeginDate());
    }

    public static final Long DELETE_LOAN_ID = 2L;

    @Test
    public void deleteWithExistsIdShouldDeleteLoan() {

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + DELETE_LOAN_ID, HttpMethod.DELETE, null, Void.class);

        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);
        assertNotNull(response);
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void deleteWithNotExistsIdShouldInternalError() {

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + NEW_LOAN_ID, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
