package com.eaglebank.eaglebank_api.v1.controller;

import com.eaglebank.eaglebank_api.v1.dto.AccountCreateDto;
import com.eaglebank.eaglebank_api.v1.dto.TransactionDto;
import com.eaglebank.eaglebank_api.v1.dto.TransactionResponseDto;
import com.eaglebank.eaglebank_api.v1.model.UserModel;
import com.eaglebank.eaglebank_api.v1.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserRepository userRepository;


    @Test
    @WithMockUser(username = "john@mail.com")
    void testExecuteAndFetchTransaction() throws Exception {
        Long accountNumber = 1L;
        AccountCreateDto accountDto = AccountCreateDto.builder()
                .name("test")
                .accountType("personal")
                .build();

        // create account for the transaction
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/accounts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountType").value("personal"));

        TransactionDto transactionDto = TransactionDto.builder()
                .amount(100.0)
                .currency("GBP")
                .type("deposit")
                .reference("Test Deposit")
                .build();
        when(userRepository.findByEmail(any(String.class))).thenReturn(UserModel.builder().id(1L).email("john@mail.com").build());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/accounts/{accountNumber}/transactions", accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.type").value("deposit"))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        TransactionResponseDto responseDto = objectMapper.readValue(responseBody, TransactionResponseDto.class);
        String tanId = responseDto.getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/accounts/{accountNumber}/transactions", accountNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactions").isArray());

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/accounts/{accountNumber}/transactions/{transactionId}", accountNumber, tanId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tanId));
    }
}