package com.example.restaurantApp.entity;

import java.math.BigDecimal;

public class OrderItem {

    private Long id;
    // Android tarafında 'order' nesnesine geri dönmeye gerek yok (sonsuz döngüden kaçınmak için sildik)
    private MenuItem menuItem;
    private Integer quantity;
    private BigDecimal unitPrice;

    public OrderItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MenuItem getMenuItem() { return menuItem; }
    public void setMenuItem(MenuItem menuItem) { this.menuItem = menuItem; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}