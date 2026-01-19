package es.bank.poc.loans.controller;

import es.bank.poc.loans.domain.dto.CreateLoanRequestDTO;
import es.bank.poc.loans.domain.dto.LoanResponseDTO;
import es.bank.poc.loans.domain.dto.UpdateStatusDTO;
import es.bank.poc.loans.mapper.LoanMapper;
import es.bank.poc.loans.service.LoanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/loans")
@Tag(
  name = "Loan Requests",
  description = "Operations related to personal loan requests"
)
public class LoanController {

  private final LoanService service;
  private final LoanMapper mapper;

  public LoanController(LoanService service, LoanMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @PostMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<LoanResponseDTO> create(
    @Valid @RequestBody CreateLoanRequestDTO dto) {

    var created = service.create(dto);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(mapper.toDto(created));
  }

  @PatchMapping("/{id}/status")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<LoanResponseDTO> updateStatus(
    @PathVariable Long id,
    @Valid @RequestBody UpdateStatusDTO dto) {

    var updated = service.updateStatus(id, dto);
    return ResponseEntity.ok(mapper.toDto(updated));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<LoanResponseDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(
      mapper.toDto(service.findById(id))
    );
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<LoanResponseDTO>> getAll() {
    return ResponseEntity.ok(
      service.findAll()
        .stream()
        .map(mapper::toDto)
        .toList()
    );
  }
}
