package com.example.restaurantApp;

public class MenuItem {
    private Long id;
    private String name;
    private Double price;
    private String category;

    // Getter methods
    public String getName() { return name; }
    public Double getPrice() { return price; }

    // Test amaçlı toString metodu
    @Override
    public String toString() {
        return name + " - " + price + " TL";
    }
}
