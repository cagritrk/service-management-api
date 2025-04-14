package com.cagriturk.servicemanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration class for the application.
 * Handles CORS configuration and other web-related settings.
 */
@Configuration
public class WebConfig {

    @Value("${app.config.allowed-origins}")
    private String[] allowedOriginUrls;

    /**
     * Configures CORS settings for the application.
     * @return WebMvcConfigurer with CORS mappings for the services endpoint
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/services/**")
                        .allowedOrigins(allowedOriginUrls)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(false);
            }
        };
    }
}