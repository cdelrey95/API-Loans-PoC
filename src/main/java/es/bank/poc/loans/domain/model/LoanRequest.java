package es.bank.poc.loans.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private BigDecimal requestedAmount;

  @Column(nullable = false, length = 3)
  private String currency;

  @Column(nullable = false)
  private String identificationDocument;

  @Column(nullable = false)
  private LocalDateTime creationDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LoanStatus status;
}
