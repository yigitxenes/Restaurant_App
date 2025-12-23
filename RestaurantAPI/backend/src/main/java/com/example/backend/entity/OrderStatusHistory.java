package com.example.backend.entity;

import com.example.backend.enums.OrderStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_status_history")
public class OrderStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK: order_status_history.order_id -> orders.id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Previous status value before the transition.
    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", columnDefinition = "enum('RECEIVED','PREPARING','READY','DELIVERED')")
    private OrderStatus oldStatus;

    // New status value after the transition.
    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false,
            columnDefinition = "enum('RECEIVED','PREPARING','READY','DELIVERED')")
    private OrderStatus newStatus;

    // FK: order_status_history.changed_by_user_id -> users.id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "changed_by_user_id", nullable = false)
    private User changedBy;

    // Timestamp managed by the database (DEFAULT CURRENT_TIMESTAMP) if configured.
    @Column(name = "changed_at", insertable = false, updatable = false)
    private LocalDateTime changedAt;

    // Required by JPA.
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

    public LocalDateTime getChangedAt() { return changedAt; }
}
