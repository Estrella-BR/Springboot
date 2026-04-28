package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {
    @Autowired
    LoanRepository loanRepository;

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
        // category.setName(dto.getName());
        loan.setBeginDate(dto.getBeginDate());
        loan.setEndDate(dto.getEndDate());
        
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
}
