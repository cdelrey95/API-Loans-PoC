package es.bank.poc.loans.repository;

import es.bank.poc.loans.domain.model.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<LoanRequest, Long> {
}