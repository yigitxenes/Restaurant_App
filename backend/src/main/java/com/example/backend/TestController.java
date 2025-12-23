package com.example.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String home() {
        return "Canavar gibi çalışıyor! 🚀 Veritabanı bağlantısı da tamam.";
    }
}