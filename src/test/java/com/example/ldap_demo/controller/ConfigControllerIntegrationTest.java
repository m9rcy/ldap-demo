package com.example.ldap_demo.controller;

import com.example.ldap_demo.config.UiConfigProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConfigController.class)
class ConfigControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Environment environment;

    @MockBean
    private UiConfigProperties configProperties;

    @Test
    void getUiConfig_shouldReturnConfigAsJson() throws Exception {
        // Setup
        when(configProperties.getExposedProperties()).thenReturn(
            Arrays.asList("app.banner.text", "app.environment")
        );
        when(environment.getProperty("app.banner.text")).thenReturn("Integration Test");
        when(environment.getProperty("app.environment")).thenReturn("test");

        // Execute and Verify
        mockMvc.perform(get("/api/config/ui"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.['app.banner.text']").value("Integration Test"))
            .andExpect(jsonPath("$.['app.environment']").value("test"));
    }
    
    @Test
    void getUiConfig_shouldReturnEmptyJsonWhenNoProperties() throws Exception {
        // Setup
        when(configProperties.getExposedProperties()).thenReturn(Arrays.asList());

        // Execute and Verify
        mockMvc.perform(get("/api/config/ui"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json("{}"));
    }
}