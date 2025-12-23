package com.example.restaurantApp.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long id;
    private String status;
    private LocalDateTime createdAt;

    private Long customerId;
    private String customerName;

    private Long tableId;
    private Integer tableNumber;

    private List<OrderItemResponse> items;

    public static class OrderItemResponse {
        private Long id;
        private Long menuItemId;
        private String menuItemName;
        private Integer quantity;
        private BigDecimal unitPrice;

        public OrderItemResponse() {}

        public OrderItemResponse(Long id, Long menuItemId, String menuItemName, Integer quantity, BigDecimal unitPrice) {
            this.id = id;
            this.menuItemId = menuItemId;
            this.menuItemName = menuItemName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        public Long getId() { return id; }
        public Long getMenuItemId() { return menuItemId; }
        public String getMenuItemName() { return menuItemName; }
        public Integer getQuantity() { return quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
    }

    public OrderResponse() {}

    public OrderResponse(Long id, String status, LocalDateTime createdAt,
                         Long customerId, String customerName,
                         Long tableId, Integer tableNumber,
                         List<OrderItemResponse> items) {
        this.id = id;
        this.status = status;
        this.createdAt = createdAt;
        this.customerId = customerId;
        this.customerName = customerName;
        this.tableId = tableId;
        this.tableNumber = tableNumber;
        this.items = items;
    }

    public Long getId() { return id; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public Long getTableId() { return tableId; }
    public Integer getTableNumber() { return tableNumber; }
    public List<OrderItemResponse> getItems() { return items; }
}
