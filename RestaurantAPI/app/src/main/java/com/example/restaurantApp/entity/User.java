package com.example.restaurantApp.entity;

import com.example.restaurantApp.enums.Role;

public class User {

    private Long id;
    private String name;
    private String email;
    private String passwordHash; // Genelde backend bunu güvenlik gereği göndermez ama modelde kalsın.
    private Role role; // String yerine Enum yaptık

    public User() {}

    public User(String name, String email, String passwordHash, Role role) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}