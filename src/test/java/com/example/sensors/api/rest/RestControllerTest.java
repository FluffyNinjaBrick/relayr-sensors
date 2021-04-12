package com.example.sensors.api.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RestController.class)
class RestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllSensors() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.get("/sensors");
        MvcResult result = mockMvc.perform(request).andReturn();


    }
}