package com.example.backend.controller;

import com.example.backend.dto.LoginRequest;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import retrofit2.http.Body;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Body LoginRequest loginRequest) {
        // Kullanıcıyı email ile bul
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());

        // Kullanıcı yoksa veya şifre yanlışsa hata dön
        // (Not: Üretim ortamında passwordHash kontrolü yapılmalı, burada basit eşleşme varsayıyoruz)
        if (user.isEmpty() || !user.get().getPasswordHash().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz email veya şifre");
        }

        // Başarılıysa kullanıcı bilgisini dön (Role bilgisi burada önemli)
        return ResponseEntity.ok(user);
    }
}