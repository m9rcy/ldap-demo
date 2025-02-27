package com.example.ldap_demo.controller;

import com.example.ldap_demo.config.UiConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    private final Environment environment;
    private final UiConfigProperties configProperties;

    @Autowired
    public ConfigController(Environment environment, UiConfigProperties configProperties) {
        this.environment = environment;
        this.configProperties = configProperties;
    }

    @GetMapping("/ui")
    public ResponseEntity<Map<String, String>> getUiConfig() {
        Map<String, String> config = new HashMap<>();

        for (String property : configProperties.getExposedProperties()) {
            String value = environment.getProperty(property);
            if (value != null) {
                config.put(property, value);
            }
        }

        // Cache for 5 minutes to reduce load, but allow refresh when needed
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES))
                .body(config);
    }
}