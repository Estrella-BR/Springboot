package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.loan.model.Loan;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanSpecification implements Specification<Loan> {

    private static final long serialVersionUID = 1L;

    private final SearchCriteria criteria;

    public LoanSpecification(SearchCriteria criteria) {

        this.criteria = criteria;
    }

    public static Specification<Loan> withFilters(Long idClient, Long idGame, LocalDate date) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (idClient != null) {
                predicates.add(cb.equal(root.get("client").get("id"), idClient));
            }

            if (idGame != null) {
                predicates.add(cb.equal(root.get("game").get("id"), idGame));
            }

            if (date != null) {
                predicates.add(cb.and(cb.lessThanOrEqualTo(root.get("beginDate"), date), cb.greaterThanOrEqualTo(root.get("endDate"), date)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public Predicate toPredicate(Root<Loan> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (criteria.getOperation().equalsIgnoreCase(":") && criteria.getValue() != null) {
            Path<String> path = getPath(root);
            if (path.getJavaType() == String.class) {
                return builder.like(path, "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(path, criteria.getValue());
            }
        }

        return null;

    }

    private Path getPath(Root<Loan> root) {

        String key = criteria.getKey();

        // Soporta propiedades anidadas: "client.id", "game.id", etc.
        if (key.contains(".")) {
            String[] parts = key.split("\\.");
            Path path = root.get(parts[0]);

            for (int i = 1; i < parts.length; i++) {
                path = path.get(parts[i]);
            }

            return path;
        }

        // Propiedad simple: "returned", "startDate", etc.
        return root.get(key);
    }

    public Specification<Loan> buildSpecification(List<SearchCriteria> criteriaList) {

        Specification<Loan> spec = Specification.where(null);

        for (SearchCriteria criteria : criteriaList) {
            spec = spec.and(new LoanSpecification(criteria));
        }

        return spec;
    }

}