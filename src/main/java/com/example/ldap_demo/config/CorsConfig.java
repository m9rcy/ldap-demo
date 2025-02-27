package com.example.ldap_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow specific origins (replace with your React app URL)
        config.addAllowedOrigin("http://localhost:3000");
        // Add production URL when ready
        // config.addAllowedOrigin("https://your-production-domain.com");
        
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setMaxAge(3600L); // 1 hour
        
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}