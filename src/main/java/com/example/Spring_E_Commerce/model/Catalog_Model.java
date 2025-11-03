package com.example.Spring_E_Commerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "products")
public class Catalog_Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Unique number is required")
    @Column(nullable = false, unique = true)
    private String unum;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0")
    @Column(nullable = false)
    private double price;

    private String image;

    // Default constructor
    public Catalog_Model() {}

    // Parameterized constructor
    public Catalog_Model(String unum, String name, String description, double price, String image) {
        this.unum = unum;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUnum() {
        return unum;
    }
    public void setUnum(String unum) {
        this.unum = unum;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
