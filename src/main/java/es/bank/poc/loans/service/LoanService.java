package es.bank.poc.loans.service;


import static es.bank.poc.loans.domain.model.LoanStatus.APPROVED;
import static es.bank.poc.loans.domain.model.LoanStatus.CANCELLED;
import static es.bank.poc.loans.domain.model.LoanStatus.REJECTED;

import es.bank.poc.loans.domain.dto.CreateLoanRequestDTO;
import es.bank.poc.loans.domain.dto.UpdateStatusDTO;
import es.bank.poc.loans.domain.model.LoanRequest;
import es.bank.poc.loans.domain.model.LoanStatus;
import es.bank.poc.loans.domain.exception.InvalidStatusTransitionException;
import es.bank.poc.loans.mapper.LoanMapper;
import es.bank.poc.loans.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {

  private final LoanRepository repository;
  private final LoanMapper mapper;

  public LoanService(LoanRepository repository, LoanMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public LoanRequest create(CreateLoanRequestDTO dto) {
    LoanRequest request = mapper.toEntity(dto);

    request.setCreationDate(LocalDateTime.now());
    request.setStatus(LoanStatus.PENDING);

    return repository.save(request);
  }

  public LoanRequest updateStatus(Long id, UpdateStatusDTO dto) {
    LoanRequest request = repository.findById(id)
      .orElseThrow(() -> new RuntimeException("Loan request not found with id: " + id));

    validateStatusTransition(request.getStatus(), dto.getStatus());
    request.setStatus(dto.getStatus());
    return repository.save(request);
  }

  public LoanRequest findById(Long id) {
    return repository.findById(id)
      .orElseThrow(() -> new RuntimeException("Loan request not found with id: " + id));
  }

  public List<LoanRequest> findAll() {
    return repository.findAll();
  }

  private void validateStatusTransition(LoanStatus current, LoanStatus newStatus) {
    switch (current) {
      case PENDING -> {
        if (newStatus == APPROVED || newStatus == REJECTED) {
          return;
        }
        throw new InvalidStatusTransitionException("From PENDING, only APPROVED or REJECTED allowed");
      }
      case APPROVED -> {
        if (newStatus == CANCELLED) {
          return;
        }
        throw new InvalidStatusTransitionException("From APPROVED, only CANCELLED allowed");
      }
      case REJECTED, CANCELLED ->
        throw new InvalidStatusTransitionException("No transitions allowed from " + current);

      default -> throw new IllegalStateException("Unexpected value: " + current);
    }
  }
}