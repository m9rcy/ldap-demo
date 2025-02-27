package com.example.ldap_demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "ui.config")
public class UiConfigProperties {
    
    private List<String> exposedProperties = new ArrayList<>();
    
    // Default constructor
    public UiConfigProperties() {
        // Default properties to expose if none are configured
        exposedProperties.add("app.banner.text");
        exposedProperties.add("app.banner.backgroundColor");
        exposedProperties.add("app.banner.textColor");
        exposedProperties.add("app.banner.enabled");
        exposedProperties.add("app.environment");
    }
    
    public List<String> getExposedProperties() {
        return exposedProperties;
    }
    
    public void setExposedProperties(List<String> exposedProperties) {
        this.exposedProperties = exposedProperties;
    }
}