package com.example.restaurantApp.entity;

import com.example.restaurantApp.enums.OrderStatus;
import java.util.List;

public class Order {

    private Long id;
    private User customer;
    private RestaurantTable table;
    private OrderStatus status; // Artık Enum kullanıyoruz
    private String createdAt;   // Tarihi String olarak almak en güvenlisidir (veya LocalDateTime)
    private List<OrderItem> items;

    public Order() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getCustomer() { return customer; }
    public void setCustomer(User customer) { this.customer = customer; }

    public RestaurantTable getTable() { return table; }
    public void setTable(RestaurantTable table) { this.table = table; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}