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

    // Getter Metodları
    public Long getId() { return id; }
    public String getStatus() { return status; }
    public List<OrderItemResponse> getItems() { return items; }

    // --- İÇ İÇE SINIFLAR (NESTED CLASSES) ---

    public static class OrderItemResponse {
        @SerializedName("quantity")
        private Integer quantity;

        // Backend "menuItem" objesi gönderiyor, biz de burada onu yakalıyoruz
        @SerializedName("menuItem")
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