package com.example.backend.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.example.backend.enums.OrderStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK: orders.customer_id -> users.id
    @JsonIgnoreProperties({"passwordHash"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    // FK: orders.table_id -> restaurant_tables.id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "table_id", nullable = false)
    private RestaurantTable table;

    // Stored as MySQL ENUM. Column definition is provided to satisfy schema validation.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('RECEIVED','PREPARING','READY','DELIVERED')")
    private OrderStatus status = OrderStatus.RECEIVED;

    // Managed by the database (DEFAULT CURRENT_TIMESTAMP).
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // Line items belonging to this order.
    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    // Required by JPA.
    public Order() {}

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public User getCustomer() { return customer; }

    public void setCustomer(User customer) { this.customer = customer; }

    public RestaurantTable getTable() { return table; }

    public void setTable(RestaurantTable table) { this.table = table; }

    public OrderStatus getStatus() { return status; }

    public void setStatus(OrderStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<OrderItem> getItems() { return items; }

    public void setItems(List<OrderItem> items) { this.items = items; }
}

