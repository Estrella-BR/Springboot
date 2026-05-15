package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.loan.exception.DateRangeExceded;
import com.ccsw.tutorial.loan.exception.EndDateBeforBeginDate;
import com.ccsw.tutorial.loan.exception.GameLoaned;
import com.ccsw.tutorial.loan.exception.LoanLimit;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
        List<Loan> loans = loanRepository.findByBeginDateLessThanEqualAndEndDateGreaterThanEqual(dto.getEndDate(), dto.getBeginDate());
        Integer clientLoans = 0;

        if (id == null) {
            loan = new Loan();
        } else {
            loan = this.get(id);
        }

        long daysBetween = dto.getBeginDate().getTime() - dto.getEndDate().getTime();
        daysBetween = Math.abs(daysBetween / (24L * 60 * 60 * 1000));

        if (dto.getBeginDate().getTime() > dto.getEndDate().getTime())
            throw new EndDateBeforBeginDate("Fecha fin no puede ser anterior a la de incio");

        if (daysBetween > 14) {
            throw new DateRangeExceded("El préstamo no puede durar más de 14 dias");
        }

        for (Loan l : loans) {
            if (l.getClient().getId().equals(dto.getClient().getId()) && l.getId() != loan.getId())
                clientLoans++;

            if (l.getGame().getId().equals(dto.getGame().getId()) && l.getId() != loan.getId())
                throw new GameLoaned("El juego ya está prestado");
        }

        if (clientLoans >= 2)
            throw new LoanLimit("El cliente ya tiene prestado 2 juegos");

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

    @Override
    public Page<Loan> findPage(LoanSearchDto dto) {

        Specification<Loan> spec = Specification.where(null);

        if (dto.getDate() != null) {
            spec = spec.and(new LoanSpecification(new SearchCriteria("beginDate", "<=", dto.getDate())));
            spec = spec.and(new LoanSpecification(new SearchCriteria("endDate", ">=", dto.getDate())));
        }

        if (dto.getClient() != null) {
            spec = spec.and(new LoanSpecification(new SearchCriteria("client.id", ":", dto.getClient().getId())));
        }

        if (dto.getGame() != null) {
            spec = spec.and(new LoanSpecification(new SearchCriteria("game.id", ":", dto.getGame().getId())));
        }

        return loanRepository.findAll(spec, dto.getPageable().getPageable());
    }

}
