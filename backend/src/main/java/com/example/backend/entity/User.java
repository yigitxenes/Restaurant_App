package com.example.backend.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.backend.enums.Role;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"passwordHash"})
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User's full name (display name).
    @Column(nullable = false)
    private String name;

    // Unique email address used for authentication/login.
    @Column(nullable = false, unique = true)
    private String email;

    // Hashed password (never store plain-text passwords).
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // Authorization role used to differentiate CUSTOMER vs STAFF accounts.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Required by JPA.
    public User() {}

    // Convenience constructor for creating user instances in code/tests.
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

    @JsonIgnore
    public String getPasswordHash() { return passwordHash; }

    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }
}
