package es.bank.poc.loans.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.bank.poc.loans.domain.dto.CreateLoanRequestDTO;
import es.bank.poc.loans.domain.dto.LoanResponseDTO;
import es.bank.poc.loans.domain.dto.UpdateStatusDTO;
import es.bank.poc.loans.domain.model.LoanRequest;
import es.bank.poc.loans.domain.model.LoanStatus;
import es.bank.poc.loans.mapper.LoanMapper;
import es.bank.poc.loans.service.LoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LoanService service;

    @MockitoBean
    private LoanMapper mapper;

    @Test
    @WithMockUser(roles = "USER")
    void create_shouldReturn201AndPendingStatus() throws Exception {
        CreateLoanRequestDTO requestDTO = createLoanRequestDTO();
        LoanRequest entity = createLoanRequest();
        LoanResponseDTO responseDTO = createLoanResponseDTO();

        when(service.create(requestDTO)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(responseDTO);

        mockMvc.perform(post("/loans")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))

          .andExpect(jsonPath("$.status").value("PENDING"))
          .andExpect(jsonPath("$.id").value(1))
          .andExpect(jsonPath("$.name").value("Pedro García"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_shouldReturnLoan() throws Exception {
        LoanRequest entity = createLoanRequest();
        LoanResponseDTO responseDTO = createLoanResponseDTO();

        when(service.findById(1L)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(responseDTO);

        mockMvc.perform(get("/loans/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Pedro García"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateStatus_shouldReturnUpdatedLoan() throws Exception {
        UpdateStatusDTO updateDTO = new UpdateStatusDTO();
        updateDTO.setStatus(LoanStatus.APPROVED);
        LoanRequest entity = createLoanRequest();
        entity.setStatus(LoanStatus.APPROVED);
        LoanResponseDTO responseDTO = createLoanResponseDTO();
        responseDTO.setStatus(LoanStatus.APPROVED);

        when(service.updateStatus(1L, updateDTO)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(responseDTO);

        mockMvc.perform(patch("/loans/1/status")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAll_shouldReturnList() throws Exception {
        LoanRequest entity = createLoanRequest();
        LoanResponseDTO responseDTO = createLoanResponseDTO();

        when(service.findAll()).thenReturn(List.of(entity));
        when(mapper.toDto(entity)).thenReturn(responseDTO);

        mockMvc.perform(get("/loans"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1));
    }

    private static LoanRequest createLoanRequest() {
        LoanRequest entity = new LoanRequest();
        entity.setId(1L);
        entity.setName("Pedro García");
        entity.setRequestedAmount(BigDecimal.valueOf(5000));
        entity.setCurrency("EUR");
        entity.setIdentificationDocument("87654321B");
        entity.setStatus(LoanStatus.PENDING);
        entity.setCreationDate(LocalDateTime.now());
        return entity;
    }

    private static CreateLoanRequestDTO createLoanRequestDTO() {
        CreateLoanRequestDTO requestDTO = new CreateLoanRequestDTO();
        requestDTO.setName("Jane Smith");
        requestDTO.setRequestedAmount(BigDecimal.valueOf(5000));
        requestDTO.setCurrency("EUR");
        requestDTO.setIdentificationDocument("87654321B");
        return requestDTO;
    }

    private static LoanResponseDTO createLoanResponseDTO() {
        LoanResponseDTO dto = new LoanResponseDTO();
        dto.setId(1L);
        dto.setName("Pedro García");
        dto.setRequestedAmount(BigDecimal.valueOf(5000));
        dto.setCurrency("EUR");
        dto.setIdentificationDocument("87654321B");
        dto.setStatus(LoanStatus.PENDING);
        dto.setCreationDate(LocalDateTime.now());
        return dto;
    }
}
