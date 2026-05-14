package com.ccsw.tutorial.loan.model;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.game.model.GameDto;

import java.util.Date;

public class LoanSearchDto {

    private PageableRequest pageable;
    private Date date;
    private ClientDto client;
    private GameDto game;

    public PageableRequest getPageable() {
        return pageable;
    }

    public void setPageable(PageableRequest pageable) {
        this.pageable = pageable;
    }

    public ClientDto getClient() {
        return client;
    }

    public void setClient(ClientDto client) {
        this.client = client;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date endDate) {
        this.date = endDate;
    }

    public GameDto getGame() {
        return game;
    }

    public void setGame(GameDto game) {
        this.game = game;
    }
}
