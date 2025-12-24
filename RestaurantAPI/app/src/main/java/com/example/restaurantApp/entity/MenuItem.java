package com.example.restaurantApp.entity;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Objects;

public class MenuItem implements Serializable {

    // EN ÖNEMLİ KISIM BURASI:
    @SerializedName("id") // Backend'deki "id" alanıyla eşleşmesini garanti eder
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("category")
    private String category;

    @SerializedName("price")
    private Double price;

    // --- Getter ve Setter Metotları ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    // Sepette (Map yapısında) düzgün çalışması için equals ve hashCode şarttır:
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return Objects.equals(id, menuItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}