package com.eaglebank.eaglebank_api.v1.controller;

import com.eaglebank.eaglebank_api.v1.dto.AccountCreateDto;
import com.eaglebank.eaglebank_api.v1.model.AccountModel;
import com.eaglebank.eaglebank_api.v1.model.UserModel;
import com.eaglebank.eaglebank_api.v1.repository.AccountRepository;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountRepository accountRepository;

    @MockitoBean
    private UserRepository userRepository;


    @Test
    @WithMockUser(username = "john@mail.com")
    void testCreateAndFetchAccount() throws Exception {
        AccountCreateDto accountDto = AccountCreateDto.builder()
                .name("test")
                .accountType("personal")
                .build();

        when(accountRepository.findAllByUserId(any(Long.class))).thenReturn(List.of(
                AccountModel.builder().name("test").accountType("personal")
                        .accountNumber(12345678L)
                        .sortCode("12-34-56")
                        .balance(1000)
                        .currency("GBP")
                        .build()
        ));
        when(accountRepository.save(any(AccountModel.class))).thenReturn(
                AccountModel.builder().name("test").accountType("personal")
                        .accountNumber(12345678L)
                        .sortCode("12-34-56")
                        .balance(1000)
                        .currency("GBP")
                        .build()
        );

        when(userRepository.findByEmail(any(String.class))).thenReturn(UserModel.builder().id(1L).email("john@mail.com").build());

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/accounts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountType").value("personal"));

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accounts").isArray());
    }
}