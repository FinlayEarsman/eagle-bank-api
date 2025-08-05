package com.eaglebank.eaglebank_api.v1.controller;

import com.eaglebank.eaglebank_api.v1.dto.AddressDto;
import com.eaglebank.eaglebank_api.v1.dto.UserRegistrationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest
@EnableWebMvc
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "john@mail.com")
    void testCreateAndFetchUser() throws Exception {
        AddressDto addressDto = AddressDto.builder()
                .line1("123 Main St")
                .line2("")
                .line3("")
                .town("Springfield")
                .county("Manchester")
                .postcode("M1 2AB")
                .build();

        UserRegistrationDto userDto = UserRegistrationDto.builder()
                .email("john@mail.com")
                .name("John Doe")
                .phoneNumber("1234567890")
                .password("password123")
                .address(addressDto)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/users").content(objectMapper.writeValueAsString(userDto)).contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john@mail.com"));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/1"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john@mail.com"));
    }

}
