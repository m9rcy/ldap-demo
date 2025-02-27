package com.example.ldap_demo.controller;

import com.example.ldap_demo.config.UiConfigProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfigControllerTest {

    @Mock
    private Environment environment;

    @Mock
    private UiConfigProperties configProperties;

    @InjectMocks
    private ConfigController configController;

    private List<String> exposedProperties;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test data
        exposedProperties = Arrays.asList(
                "app.banner.text",
                "app.banner.backgroundColor",
                "app.banner.textColor",
                "app.banner.enabled",
                "app.environment"
        );

        when(configProperties.getExposedProperties()).thenReturn(exposedProperties);
    }

    @Test
    void getUiConfig_shouldReturnAllConfiguredProperties() {
        // Setup
        when(environment.getProperty("app.banner.text")).thenReturn("Test Environment");
        when(environment.getProperty("app.banner.backgroundColor")).thenReturn("#FF0000");
        when(environment.getProperty("app.banner.textColor")).thenReturn("#FFFFFF");
        when(environment.getProperty("app.banner.enabled")).thenReturn("true");
        when(environment.getProperty("app.environment")).thenReturn("test");

        // Execute
        ResponseEntity<Map<String, String>> response = configController.getUiConfig();
        Map<String, String> config = response.getBody();

        // Verify
        assertNotNull(config);
        assertEquals(5, config.size());
        assertEquals("Test Environment", config.get("app.banner.text"));
        assertEquals("#FF0000", config.get("app.banner.backgroundColor"));
        assertEquals("#FFFFFF", config.get("app.banner.textColor"));
        assertEquals("true", config.get("app.banner.enabled"));
        assertEquals("test", config.get("app.environment"));
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify interactions
        verify(configProperties, times(1)).getExposedProperties();
        verify(environment, times(1)).getProperty("app.banner.text");
        verify(environment, times(1)).getProperty("app.banner.backgroundColor");
        verify(environment, times(1)).getProperty("app.banner.textColor");
        verify(environment, times(1)).getProperty("app.banner.enabled");
        verify(environment, times(1)).getProperty("app.environment");
    }

    @Test
    void getUiConfig_shouldSkipNullProperties() {
        // Setup - only provide values for some properties
        when(environment.getProperty("app.banner.text")).thenReturn("Test Environment");
        when(environment.getProperty("app.environment")).thenReturn("test");

        // Other properties will return null by default with the mock

        // Execute
        ResponseEntity<Map<String, String>> response = configController.getUiConfig();
        Map<String, String> config = response.getBody();

        // Verify
        assertNotNull(config);
        assertEquals(2, config.size());
        assertEquals("Test Environment", config.get("app.banner.text"));
        assertEquals("test", config.get("app.environment"));

        // Verify these keys are not in the map
        assertFalse(config.containsKey("app.banner.backgroundColor"));
        assertFalse(config.containsKey("app.banner.textColor"));
        assertFalse(config.containsKey("app.banner.enabled"));
    }

    @Test
    void getUiConfig_shouldReturnEmptyMapWhenNoPropertiesFound() {
        // Setup - all properties return null

        // Execute
        ResponseEntity<Map<String, String>> response = configController.getUiConfig();
        Map<String, String> config = response.getBody();

        // Verify
        assertNotNull(config);
        assertTrue(config.isEmpty());
    }

    @Test
    void getUiConfig_shouldHandleEmptyExposedPropertiesList() {
        // Setup - empty list of properties to expose
        when(configProperties.getExposedProperties()).thenReturn(List.of());

        // Execute
        ResponseEntity<Map<String, String>> response = configController.getUiConfig();
        Map<String, String> config = response.getBody();

        // Verify
        assertNotNull(config);
        assertTrue(config.isEmpty());
    }
}