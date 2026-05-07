package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface LoanRepository extends CrudRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {

    @Override
    @EntityGraph(attributePaths = { "client", "game", "beginDate" })
    Page<Loan> findAll(Specification<Loan> spec, Pageable pageable);

    @Query("SELECT l FROM Loan l WHERE l.beginDate <= :endDate AND l.endDate >= :beginDate")
    List<Loan> findLoansInDateRange(@Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

}
