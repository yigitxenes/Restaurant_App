package com.example.restaurantApp.entity;

import java.math.BigDecimal;

public class MenuItem {

    private Long id;
    private String name;
    private BigDecimal price;
    private String category;
    private Boolean isActive;

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