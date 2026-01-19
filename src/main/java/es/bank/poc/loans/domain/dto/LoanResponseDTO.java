package es.bank.poc.loans.domain.dto;

import es.bank.poc.loans.domain.model.LoanStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LoanResponseDTO {
  private Long id;
  private String name;
  private BigDecimal requestedAmount;
  private String currency;
  private String identificationDocument;
  private LocalDateTime creationDate;
  private LoanStatus status;
}