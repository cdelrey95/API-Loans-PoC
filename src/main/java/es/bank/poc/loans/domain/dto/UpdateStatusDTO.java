package es.bank.poc.loans.domain.dto;

import es.bank.poc.loans.domain.model.LoanStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusDTO {
  @NotNull(message = "Status is required")
  private LoanStatus status;
}