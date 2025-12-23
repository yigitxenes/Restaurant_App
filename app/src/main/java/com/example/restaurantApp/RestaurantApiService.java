package com.example.restaurantApp;

import com.example.restaurantApp.dto.LoginRequest;
import com.example.restaurantApp.entity.RestaurantTable;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestaurantApiService {
    @POST("/api/auth/login")
    Call<User> login(@Body LoginRequest request);

    // Sipariş Ver
    @POST("/api/orders")
    Call<OrderResponse> placeOrder(@Body CreateOrderRequest request);

    // Sipariş Durumu Sorgula
    @GET("/api/orders/{id}")
    Call<OrderResponse> getOrderID();

    // Personel: Masaları Getir (StaffOrderController)
    @GET("/api/staff/tables")
    Call<List<RestaurantTable>> getTables();

    Call<List<MenuItem>> getActiveMenu();
}