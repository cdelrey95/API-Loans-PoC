package es.bank.poc.loans.service;

import es.bank.poc.loans.domain.dto.CreateLoanRequestDTO;
import es.bank.poc.loans.domain.dto.UpdateStatusDTO;
import es.bank.poc.loans.domain.model.LoanRequest;
import es.bank.poc.loans.domain.model.LoanStatus;
import es.bank.poc.loans.domain.exception.InvalidStatusTransitionException;
import es.bank.poc.loans.mapper.LoanMapper;
import es.bank.poc.loans.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

  @Mock
  private LoanRepository repository;

  @Mock
  private LoanMapper mapper;

  @InjectMocks
  private LoanService service;

  @Test
  void create_shouldSetPendingStatus() {
    CreateLoanRequestDTO dto = new CreateLoanRequestDTO();
    dto.setName("Pedro García");
    dto.setRequestedAmount(BigDecimal.valueOf(10000));
    dto.setCurrency("EUR");
    dto.setIdentificationDocument("12345678A");

    LoanRequest loanRequest = createLoanRequest();

    when(mapper.toEntity(dto)).thenReturn(loanRequest);

    when(repository.save(any(LoanRequest.class))).thenAnswer(invocation -> {
      LoanRequest argument = invocation.getArgument(0);
      argument.setId(1L);
      return argument;
    });

    LoanRequest result = service.create(dto);

    assertEquals(LoanStatus.PENDING, result.getStatus());
    assertEquals("Pedro García", result.getName());
    assertNotNull(result.getCreationDate());

    verify(mapper).toEntity(dto);
    verify(repository).save(any(LoanRequest.class));
  }

  @Test
  void updateStatus_validTransition_shouldUpdate() {
    LoanRequest request = new LoanRequest();
    request.setId(1L);
    request.setStatus(LoanStatus.PENDING);

    when(repository.findById(1L)).thenReturn(Optional.of(request));
    when(repository.save(any(LoanRequest.class))).thenReturn(request);

    UpdateStatusDTO dto = new UpdateStatusDTO();
    dto.setStatus(LoanStatus.APPROVED);

    LoanRequest result = service.updateStatus(1L, dto);

    assertEquals(LoanStatus.APPROVED, result.getStatus());
    verify(repository).save(request);
  }

  @Test
  void findById_shouldReturnLoan() {
    LoanRequest request = createLoanRequest();
    request.setId(1L);

    when(repository.findById(1L)).thenReturn(Optional.of(request));

    LoanRequest result = service.findById(1L);

    assertEquals(1L, result.getId());
    assertEquals("Pedro García", result.getName());
  }

  @Test
  void findAll_shouldReturnList() {
    LoanRequest request1 = createLoanRequest();
    request1.setId(1L);
    LoanRequest request2 = createLoanRequest();
    request2.setId(2L);

    when(repository.findAll()).thenReturn(List.of(request1, request2));

    List<LoanRequest> result = service.findAll();

    assertEquals(2, result.size());
    assertEquals(1L, result.get(0).getId());
  }

  private static LoanRequest createLoanRequest() {
    LoanRequest created = new LoanRequest();
    created.setName("Pedro García");
    created.setRequestedAmount(BigDecimal.valueOf(5000));
    created.setCurrency("EUR");
    created.setIdentificationDocument("87654321B");
    return created;
  }
}