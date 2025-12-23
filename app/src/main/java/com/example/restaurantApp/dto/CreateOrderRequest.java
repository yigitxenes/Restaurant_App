package com.example.restaurantApp.dto;

import java.util.List;

public class CreateOrderRequest {

    private Long customerId;
    private Long tableId;
    private List<OrderLineRequest> items;

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getTableId() { return tableId; }
    public void setTableId(Long tableId) { this.tableId = tableId; }

    public List<OrderLineRequest> getItems() { return items; }
    public void setItems(List<OrderLineRequest> items) { this.items = items; }
}
