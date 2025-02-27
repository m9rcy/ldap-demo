package com.example.ldap_demo.config;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UiConfigPropertiesTest {

    @Test
    void constructor_shouldInitializeDefaultProperties() {
        // Execute
        UiConfigProperties properties = new UiConfigProperties();
        List<String> exposedProperties = properties.getExposedProperties();

        // Verify
        assertNotNull(exposedProperties);
        assertEquals(5, exposedProperties.size());
        assertTrue(exposedProperties.contains("app.banner.text"));
        assertTrue(exposedProperties.contains("app.banner.backgroundColor"));
        assertTrue(exposedProperties.contains("app.banner.textColor"));
        assertTrue(exposedProperties.contains("app.banner.enabled"));
        assertTrue(exposedProperties.contains("app.environment"));
    }

    @Test
    void setExposedProperties_shouldOverrideDefaultProperties() {
        // Setup
        UiConfigProperties properties = new UiConfigProperties();
        List<String> customProperties = Arrays.asList(
                "custom.property1",
                "custom.property2"
        );

        // Execute
        properties.setExposedProperties(customProperties);
        List<String> retrievedProperties = properties.getExposedProperties();

        // Verify
        assertEquals(customProperties, retrievedProperties);
        assertEquals(2, retrievedProperties.size());
        assertTrue(retrievedProperties.contains("custom.property1"));
        assertTrue(retrievedProperties.contains("custom.property2"));

        // Verify defaults are overridden
        assertFalse(retrievedProperties.contains("app.banner.text"));
    }
}