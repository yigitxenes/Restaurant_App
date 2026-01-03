package com.example.restaurantApp.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OrderResponse {

    @SerializedName("id")
    private Long id;

    @SerializedName("status")
    private String status;

    @SerializedName("items")
    private List<OrderItemResponse> items;

    // Getter'lar
    public Long getId() { return id; }
    public String getStatus() { return status; }
    public List<OrderItemResponse> getItems() { return items; }

    // --- İÇ SINIFLAR (Nested Classes) ---

    public static class OrderItemResponse {
        @SerializedName("quantity")
        private Integer quantity;

        @SerializedName("menuItem") // Backend'deki "menuItem" objesini yakalar
        private MenuItemDTO menuItem;

        public Integer getQuantity() { return quantity; }
        public MenuItemDTO getMenuItem() { return menuItem; }
    }

    public static class MenuItemDTO {
        @SerializedName("name")
        private String name;

        @SerializedName("price")
        private Double price;

        public String getName() { return name; }
        public Double getPrice() { return price; }
    }
}