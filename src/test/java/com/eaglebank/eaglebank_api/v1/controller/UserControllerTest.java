package com.eaglebank.eaglebank_api.v1.controller;

import com.eaglebank.eaglebank_api.v1.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = {UserController.class})
@EnableWebMvc
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceImpl userService;

    @Test
    void testCreateUser() throws Exception {
        String userJson = "{ \"username\": \"John\", \"email\": \"john@mail.com\", \"password\": \"john123\" }";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/users").content(userJson)
                .param("id", "1").contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void testGetUser() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/users")
                .param("id", "1");
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

}
