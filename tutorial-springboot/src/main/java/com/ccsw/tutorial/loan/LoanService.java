package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;

import java.util.List;

public interface LoanService {
    /**
     * Recupera una {@link Loan} a partir de su ID
     *
     * @param id PK de la entidad
     * @return {@link Loan}
     */
    Loan get(Long id);

    /**
     * Método para recuperar todas las {@link Loan}
     *
     * @return {@link List} de {@link Loan}
     */
    List<Loan> findAll();

    /**
     * Método para crear o actualizar una {@link Loan}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, LoanDto dto);

    /**
     * Método para borrar una {@link Loan}
     *
     * @param id PK de la entidad
     */
    void delete(Long id) throws Exception;
}
