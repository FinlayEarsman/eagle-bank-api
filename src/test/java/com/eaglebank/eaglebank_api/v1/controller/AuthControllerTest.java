package com.eaglebank.eaglebank_api.v1.controller;

import com.eaglebank.eaglebank_api.v1.dto.AuthRequestDto;
import com.eaglebank.eaglebank_api.v1.model.UserModel;
import com.eaglebank.eaglebank_api.v1.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        UserModel user = UserModel.builder().email("test@mail.com")
                .name("test")
                .phoneNumber("1234567890")
                .address(null)
                .password(passwordEncoder.encode( "password123"))
                .build();
        userRepository.save(user);
    }

    @Test
    void testValidAuthenticateReturnJwtToken() throws Exception {
        AuthRequestDto request = new AuthRequestDto();
        request.setUsername("test@mail.com");
        request.setPassword("password123");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}
