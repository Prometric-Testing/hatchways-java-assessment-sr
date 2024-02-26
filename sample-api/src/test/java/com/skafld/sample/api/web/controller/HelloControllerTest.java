package com.skafld.sample.api.web.controller;

import com.skafld.sample.api.web.dto.HelloRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(HelloController.class)
@AutoConfigureMockMvc
public class HelloControllerTest {
    @Autowired
    public MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void greetingShouldReturnBadRequestWhenNameContainsSpecialCharacters() throws Exception {
        HelloRequest request = new HelloRequest("Invalid#Name");

        mockMvc.perform(post("/api/v1/hello/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void greetingShouldReturnBadRequestWhenNameContainsNumbers() throws Exception {
        HelloRequest request = new HelloRequest("Invalid123");

        mockMvc.perform(post("/api/v1/hello/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void greetingShouldReturnHelloMessageWithCorrectContentType() throws Exception {
        HelloRequest request = new HelloRequest("Friend");

        mockMvc.perform(post("/api/v1/hello/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
