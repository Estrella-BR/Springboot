package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Loan", description = "API of Loan")
@RequestMapping(value = "/loan")
@RestController
@CrossOrigin(origins = "*")
public class LoanController {
    @Autowired
    LoanService loanService;

    @Autowired
    ModelMapper mapper;

    /**
     * Método para recuperar todas las {@link Loan}
     *
     * @return {@link List} de {@link LoanDto}
     */
    @Operation(summary = "Find", description = "Method that return a list of Categories")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<LoanDto> findAll() {

        List<Loan> categories = this.loanService.findAll();

        return categories.stream().map(e -> mapper.map(e, LoanDto.class)).collect(Collectors.toList());
    }

    /**
     * Método para crear o actualizar una {@link Loan}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    @Operation(summary = "Save or Update", description = "Method that saves or updates a Loan")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody LoanDto dto) {
        this.loanService.save(id, dto);
    }

    /**
     * Método para borrar una {@link Loan}
     *
     * @param id PK de la entidad
     */
    @Operation(summary = "Delete", description = "Method that deletes a Loan")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {

        this.loanService.delete(id);
    }

}
