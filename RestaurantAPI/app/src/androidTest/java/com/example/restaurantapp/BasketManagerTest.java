package com.example.restaurantapp; // Paket isminin doğru olduğuna emin ol

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.restaurantApp.BasketManager;
import com.example.restaurantApp.entity.MenuItem;

public class BasketManagerTest {

    private BasketManager basketManager;

    @Before
    public void setUp() {
        basketManager = BasketManager.getInstance();
        basketManager.clearBasket();
    }

    @Test
    public void testAddItem() {
        // Constructor ile kolayca oluşturabilirsin
        MenuItem item = new MenuItem(1L, "Test Burger", 100.0);

        basketManager.addItem(item);

        assertEquals("Sepette 1 çeşit ürün olmalı", 1, basketManager.getItems().size());
        assertTrue("Eklenen ürün sepette bulunmalı", basketManager.getItems().containsKey(item));
    }

    @Test
    public void testIncreaseQuantity() {
        MenuItem item = new MenuItem(1L, "Test Burger", 50.0);

        basketManager.addItem(item);
        basketManager.addItem(item);

        assertEquals("Sepette hala 1 çeşit ürün olmalı", 1, basketManager.getItems().size());
        assertEquals("Ürün adedi 2 olmalı", (Integer) 2, basketManager.getItems().get(item));
    }

    @Test
    public void testTotalPriceCalculation() {
        MenuItem item1 = new MenuItem(1L, "Burger", 100.0);
        MenuItem item2 = new MenuItem(2L, "Kola", 50.0);

        basketManager.addItem(item1); // 100
        basketManager.addItem(item2); // 50
        basketManager.addItem(item2); // +50

        // Toplam: 200.0 olmalı
        assertEquals(200.0, basketManager.getTotalPrice(), 0.01);
    }

    @Test
    public void testClearBasket() {
        MenuItem item = new MenuItem(1L, "Su", 10.0);
        basketManager.addItem(item);

        basketManager.clearBasket();

        assertTrue("Sepet boş olmalı", basketManager.getItems().isEmpty());
        assertEquals(0.0, basketManager.getTotalPrice(), 0.01);
    }
}