package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {
    @Autowired
    LoanRepository loanRepository;

    @Autowired
    GameService gameService;

    @Autowired
    ClientService clientService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Loan get(Long id) {

        return this.loanRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Loan> findAll() {

        return (List<Loan>) this.loanRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, LoanDto dto) {

        Loan loan;
        List<Loan> loans = loanRepository.findLoansInDateRange(dto.getBeginDate(), dto.getEndDate());
        Integer clientLoans = 0;

        if (id == null) {
            loan = new Loan();
        } else {
            loan = this.get(id);
        }

        long daysBetween = dto.getBeginDate().getTime() - dto.getEndDate().getTime();
        daysBetween = Math.abs(daysBetween / (24L * 60 * 60 * 1000));

        if (daysBetween < 0 || daysBetween > 16) {
            throw new RuntimeException("El préstamo no puede durar más de 16 días" + daysBetween);
        }

        for (Loan l : loans) {
            if (Objects.equals(l.getClient().getId(), dto.getClient().getId()) && l.getId() != loan.getId())
                clientLoans++;
            if (l.getGame().getId() == dto.getGame().getId() && l.getId() != loan.getId())
                throw new RuntimeException("El juego ya está prestado");
        }

        if (clientLoans == 2)
            throw new RuntimeException("El cliente ya tiene prestado 2 juegos");

        BeanUtils.copyProperties(dto, loan, "id", "game", "client");
        loan.setClient(clientService.get(dto.getClient().getId()));
        loan.setGame(gameService.get(dto.getGame().getId()));
        this.loanRepository.save(loan);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }

        this.loanRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Loan> findPage(LoanSearchDto dto) {

        PageableRequest pageableRequest = dto.getPageable();

        Pageable pageable = PageRequest.of(pageableRequest.getPageNumber(), pageableRequest.getPageSize());

        Specification<Loan> spec = buildSpecification(dto.getCriteria());

        return this.loanRepository.findAll(spec, pageable);
    }

    private Specification<Loan> buildSpecification(List<SearchCriteria> criteriaList) {

        Specification<Loan> spec = Specification.where(null);

        if (criteriaList != null) {
            for (SearchCriteria criteria : criteriaList) {
                spec = spec.and(new LoanSpecification(criteria));
            }
        }

        return spec;
    }

    @Override
    public Page<Loan> find(Long idClient, Long idGame, Date date, Pageable pageable) {

        Specification<Loan> spec = LoanSpecification.withFilters(idClient, idGame, date);

        return loanRepository.findAll(spec, pageable);

    }
}
