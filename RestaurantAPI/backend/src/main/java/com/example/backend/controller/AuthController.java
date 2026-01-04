package com.example.backend.controller;

import com.example.backend.dto.LoginRequest;
import com.example.backend.entity.User;
import com.example.backend.enums.Role;
import com.example.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Authentication controller handling user login and registration.
 * Uses BCrypt for secure password hashing.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * User login endpoint.
     * Validates email and password using BCrypt hash comparison.
     *
     * @param loginRequest contains email and password
     * @return User object if credentials are valid, 401 otherwise
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Login attempt for email: {}", loginRequest.getEmail());

        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());

        if (userOpt.isEmpty()) {
            logger.warn("Login failed: User not found - {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz email veya şifre");
        }

        User user = userOpt.get();

        // BCrypt password verification
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            logger.warn("Login failed: Invalid password for user - {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz email veya şifre");
        }

        logger.info("Login successful for user: {}", loginRequest.getEmail());
        return ResponseEntity.ok(user);
    }

    /**
     * User registration endpoint.
     * Creates a new user with BCrypt hashed password.
     *
     * @param registerRequest map containing: name, email, password, role
     * @return Success message with user ID, or error message
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> registerRequest) {
        String name = registerRequest.get("name");
        String email = registerRequest.get("email");
        String password = registerRequest.get("password");
        String roleStr = registerRequest.getOrDefault("role", "CUSTOMER");

        logger.info("Registration attempt for email: {}", email);

        // Validate required fields
        if (name == null || email == null || password == null) {
            logger.warn("Registration failed: Missing required fields");
            return ResponseEntity.badRequest().body("Name, email, and password are required");
        }

        // Check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            logger.warn("Registration failed: Email already exists - {}", email);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Bu email adresi zaten kullanılıyor");
        }

        // Parse role
        Role role;
        try {
            role = Role.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Registration failed: Invalid role - {}", roleStr);
            return ResponseEntity.badRequest().body("Geçersiz rol. CUSTOMER veya STAFF olmalı");
        }

        // Create new user with hashed password
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPasswordHash(passwordEncoder.encode(password));
        newUser.setRole(role);

        User savedUser = userRepository.save(newUser);
        logger.info("Registration successful for user: {} (ID: {})", email, savedUser.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Kullanıcı başarıyla oluşturuldu");
        response.put("userId", savedUser.getId());
        response.put("email", savedUser.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
