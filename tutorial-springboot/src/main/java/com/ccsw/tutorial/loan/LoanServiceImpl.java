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

import java.time.LocalDate;
import java.util.List;

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

        if (id == null) {
            loan = new Loan();
        } else {
            loan = this.get(id);
        }

        long daysBetween = dto.getBeginDate().getDayOfMonth() - dto.getEndDate().getDayOfMonth();
        daysBetween = Math.abs(daysBetween);

        if (daysBetween < 0 || daysBetween > 16) {
            throw new RuntimeException("El préstamo no puede durar más de 16 días" + daysBetween);
        }

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
    public Page<Loan> find(Long idClient, Long idGame, LocalDate date, Pageable pageable) {

        Specification<Loan> spec = LoanSpecification.withFilters(idClient, idGame, date);

        return loanRepository.findAll(spec, pageable);

    }
}
