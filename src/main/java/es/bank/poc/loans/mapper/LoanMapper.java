package es.bank.poc.loans.mapper;

import es.bank.poc.loans.domain.dto.CreateLoanRequestDTO;
import es.bank.poc.loans.domain.dto.LoanResponseDTO;
import es.bank.poc.loans.domain.model.LoanRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanMapper {

  LoanRequest toEntity(CreateLoanRequestDTO dto);

  LoanResponseDTO toDto(LoanRequest entity);

}