package com.example.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "menu_items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Menu item display name (e.g., "Cheeseburger").
    @Column(nullable = false, length = 150)
    private String name;

    // Current menu price. Order items should copy this value into unit_price at order time.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // Category label for grouping (e.g., "Food", "Drink").
    @Column(length = 80)
    private String category;

    // Soft-availability flag (1 = active, 0 = inactive).
    @Column(name = "is_active", nullable = false, columnDefinition = "tinyint(1)")
    private Boolean isActive = true;

    // Required by JPA.
    public MenuItem() {}

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }

    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    public Boolean getIsActive() { return isActive; }

    public void setIsActive(Boolean active) { isActive = active; }
}
