package com.example.restaurantApp;

import com.example.restaurantApp.entity.MenuItem;

import java.util.HashMap;
import java.util.Map;

public class BasketManager {

    // Tek bir tane sepet olsun (Singleton yapısı)
    private static BasketManager instance;

    // Ürün ID'si ve Adedi (Hangi üründen kaç tane var?)
    private Map<MenuItem, Integer> items = new HashMap<>();

    private BasketManager() {}

    public static synchronized BasketManager getInstance() {
        if (instance == null) {
            instance = new BasketManager();
        }
        return instance;
    }

    // Sepete ürün ekle
    public void addItem(MenuItem item) {
        if (items.containsKey(item)) {
            items.put(item, items.get(item) + 1); // Varsa 1 artır
        } else {
            items.put(item, 1); // Yoksa 1 tane ekle
        }
    }

    // Sepetten ürün sil (Opsiyonel)
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

    // Sepeti temizle (Sipariş verince lazım olacak)
    public void clearBasket() {
        items.clear();
    }

    // Sepetteki ürünleri getir
    public Map<MenuItem, Integer> getItems() {
        return items;
    }

    // Toplam tutarı hesapla
    public double getTotalPrice() {
        double total = 0;
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice().doubleValue() * entry.getValue();
        }
        return total;
    }
}