package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.config.ResponsePage;
import com.ccsw.tutorial.game.model.GameDto;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoanIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/loan";

    private static final int TOTAL_LOANS = 6;
    private static final int PAGE_SIZE = 5;

    private static final Long EXISTS_GAME = 6L;
    private static final Long EXISTS_CLIENT = 3L;
    private static final Long NOT_EXISTS_GAME = 7L;
    private static final Long NOT_EXISTS_CLIENT = 4L;

    private static final Long MODIFY_LOAN_ID = 5L;
    private static final Long DELETE_LOAN_ID = 2L;
    private static final Long NEW_LOAN_ID = 7L;

    private static final LocalDate NEW_LOAN_BEGINDATE = LocalDate.of(2025, 8, 15);
    private static final LocalDate NEW_LOAN_ENDDATE = LocalDate.of(2025, 8, 19);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final ParameterizedTypeReference<ResponsePage<LoanDto>> responseType = new ParameterizedTypeReference<>() {
    };

    /* ---------------- FIND ALL ---------------- */

    @Test
    public void findAllShouldReturnAllLoans() {

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
    }

    /* ---------------- SAVE ---------------- */

    @Test
    public void saveWithoutIdShouldCreateNewLoan() {

        ClientDto client = new ClientDto();
        client.setId(1L);

        GameDto game = new GameDto();
        game.setId(1L);

        LoanDto dto = new LoanDto();
        dto.setBeginDate(NEW_LOAN_BEGINDATE);
        dto.setEndDate(NEW_LOAN_ENDDATE);
        dto.setClient(client);
        dto.setGame(game);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);

        assertEquals(TOTAL_LOANS + 1, response.getBody().getTotalElements());

        LoanDto loan = response.getBody().getContent().stream().filter(l -> l.getId().equals(NEW_LOAN_ID)).findFirst().orElse(null);

        assertNotNull(loan);
        assertEquals(NEW_LOAN_BEGINDATE, loan.getBeginDate());
    }

    /* ---------------- MODIFY ---------------- */

    @Test
    public void modifyWithExistIdShouldModifyLoan() {

        ClientDto client = new ClientDto();
        client.setId(1L);

        GameDto game = new GameDto();
        game.setId(1L);

        LoanDto dto = new LoanDto();
        dto.setBeginDate(NEW_LOAN_BEGINDATE);
        dto.setEndDate(NEW_LOAN_ENDDATE);
        dto.setClient(client);
        dto.setGame(game);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + MODIFY_LOAN_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);

        LoanDto loan = response.getBody().getContent().stream().filter(l -> l.getId().equals(MODIFY_LOAN_ID)).findFirst().orElse(null);

        assertNotNull(loan);
        assertEquals(NEW_LOAN_BEGINDATE, loan.getBeginDate());
    }

    /* ---------------- DELETE ---------------- */

    @Test
    public void deleteWithExistsIdShouldDeleteLoan() {

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + DELETE_LOAN_ID, HttpMethod.DELETE, null, Void.class);

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);

        assertEquals(TOTAL_LOANS - 1, response.getBody().getTotalElements());
    }

    @Test
    public void deleteWithNotExistsIdShouldInternalError() {

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + NEW_LOAN_ID, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /* ---------------- PAGINATION ---------------- */

    @Test
    public void findFirstPageWithFiveSizeShouldReturnFirstFiveResults() {

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "?page=0&size=" + PAGE_SIZE, HttpMethod.GET, null, responseType);

        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    /* ---------------- FILTERS ---------------- */

    @Test
    public void findExistsGameShouldReturnLoans() {

        String url = UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH).queryParam("idGame", EXISTS_GAME).toUriString();

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsClientShouldReturnLoans() {

        String url = UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH).queryParam("idClient", EXISTS_CLIENT).toUriString();

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsGameAndClientShouldReturnLoans() {

        String url = UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH).queryParam("idGame", EXISTS_GAME).queryParam("idClient", EXISTS_CLIENT).toUriString();

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    public void findNotExistsGameOrClientShouldReturnEmpty() {

        String url = UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH).queryParam("idGame", NOT_EXISTS_GAME).queryParam("idClient", NOT_EXISTS_CLIENT).toUriString();

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

        assertEquals(0, response.getBody().getTotalElements());
    }
}