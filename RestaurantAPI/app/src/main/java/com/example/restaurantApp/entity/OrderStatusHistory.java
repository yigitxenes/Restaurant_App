package com.example.restaurantApp.entity;

import com.example.restaurantApp.enums.OrderStatus;

public class OrderStatusHistory {

    private Long id;
    // Detaylı Order nesnesi yerine sadece ID tutmak bazen daha güvenlidir ama burada nesne kalsın.
    private Order order;
    private OrderStatus oldStatus;
    private OrderStatus newStatus;
    private User changedBy;
    private String changedAt;

    public OrderStatusHistory() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public OrderStatus getOldStatus() { return oldStatus; }
    public void setOldStatus(OrderStatus oldStatus) { this.oldStatus = oldStatus; }

    public OrderStatus getNewStatus() { return newStatus; }
    public void setNewStatus(OrderStatus newStatus) { this.newStatus = newStatus; }

    public User getChangedBy() { return changedBy; }
    public void setChangedBy(User changedBy) { this.changedBy = changedBy; }

    public String getChangedAt() { return changedAt; }
    public void setChangedAt(String changedAt) { this.changedAt = changedAt; }
}