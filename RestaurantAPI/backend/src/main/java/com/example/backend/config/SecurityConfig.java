package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Security configuration for the Restaurant API.
 * Provides BCrypt password encoder for secure password hashing.
 */
@Configuration
public class SecurityConfig {

    /**
     * BCrypt password encoder bean.
     * Uses strength 10 (default) for a good balance between security and
     * performance.
     * 
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
