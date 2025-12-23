package com.example.restaurantApp.entity;
public class OrderStatusHistory {

    private Long id;
    private Order order; // Order sınıfını birazdan oluşturacağız, şimdilik hata verebilir.
    private OrderStatus oldStatus;
    private OrderStatus newStatus;
    private User changedBy; // User sınıfını az önce oluşturmuştuk.
    private String changedAt; // Tarih verisini String olarak almak en güvenlisidir.

    // Boş Constructor
    public OrderStatusHistory() {}

    // Getter ve Setter'lar
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
