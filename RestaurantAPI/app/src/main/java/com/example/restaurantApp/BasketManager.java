package com.example.restaurantApp;

import com.example.restaurantApp.entity.MenuItem;

import java.util.HashMap;
import java.util.Map;

public class BasketManager {

    // Singleton instance
    private static BasketManager instance;

    // Item ID and Quantity map
    private Map<MenuItem, Integer> items = new HashMap<>();

    private BasketManager() {
    }

    public static synchronized BasketManager getInstance() {
        if (instance == null) {
            instance = new BasketManager();
        }
        return instance;
    }

    // Add item to basket
    public void addItem(MenuItem item) {
        if (items.containsKey(item)) {
            items.put(item, items.get(item) + 1); // Increment count
        } else {
            items.put(item, 1); // Add new
        }
    }

    // Remove item
    public void removeItem(MenuItem item) {
        if (items.containsKey(item)) {
            int currentQty = items.get(item);
            if (currentQty > 1) {
                items.put(item, currentQty - 1);
            } else {
                items.remove(item);
            }
        }
    }

    // Clear basket
    public void clearBasket() {
        items.clear();
    }

    // Get items
    public Map<MenuItem, Integer> getItems() {
        return items;
    }

    // Calculate total price
    public double getTotalPrice() {
        double total = 0;
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice().doubleValue() * entry.getValue();
        }
        return total;
    }
}