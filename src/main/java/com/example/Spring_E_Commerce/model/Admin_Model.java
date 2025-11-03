package com.example.Spring_E_Commerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "admins")
public class Admin_Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Password is required")
    @Column(name = "password")
    private String password;

    // Default constructor
    public Admin_Model() {}

    // Parameterized constructor
    public Admin_Model(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
