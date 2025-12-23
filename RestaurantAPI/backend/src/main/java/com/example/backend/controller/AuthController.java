package com.example.backend.controller;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.backend.dto.LoginRequest;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // RestController, RequestMapping, PostMapping burada
import java.util.Optional;

// SİL: import retrofit2.http.Body;  <-- BU HATALI
// EKLE (Eğer yukarıdaki .* içinde gelmediyse):
// import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth") // Burası zaten doğru
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    // DÜZELTME: @Body yerine @RequestBody yazmalısın!
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());

        if (user.isEmpty() || !user.get().getPasswordHash().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz email veya şifre");
        }
        return ResponseEntity.ok(user);
    }
}