package es.bank.poc.loans.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(example = "{\"name\":\"Pedro García\",\"requestedAmount\":5000,\"currency\":\"EUR\",\"identificationDocument\":\"87654321B\"}")
public class CreateLoanRequestDTO {
  @NotBlank(message = "Name is required")
  private String name;

  @NotNull(message = "Amount is required")
  @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
  private BigDecimal requestedAmount;

  @NotBlank(message = "Currency is required")
  private String currency;

  @NotBlank(message = "Identification document is required")
  private String identificationDocument;
}