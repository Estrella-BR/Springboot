package com.ccsw.tutorial.loan.model;

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.common.pagination.PageableRequest;

import java.util.List;

public class LoanSearchDto {
    private PageableRequest pageable;
    private List<SearchCriteria> criteria;

    public PageableRequest getPageable() {
        return pageable;
    }

    public void setPageable(PageableRequest pageable) {
        this.pageable = pageable;
    }

    public List<SearchCriteria> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<SearchCriteria> criteria) {
        this.criteria = criteria;
    }

}
