package com.example.restaurantApp;

import com.example.restaurantApp.dto.CreateOrderRequest;
import com.example.restaurantApp.dto.LoginRequest;
import com.example.restaurantApp.dto.OrderResponse;
import com.example.restaurantApp.entity.MenuItem;
import com.example.restaurantApp.entity.RestaurantTable;
import com.example.restaurantApp.entity.User; // User importu ekledim

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path; // Path importu gerekli

public interface RestaurantApiService {

    @POST("/api/auth/login")
    Call<User> login(@Body LoginRequest request);

    @POST("/api/orders")
    Call<OrderResponse> placeOrder(@Body CreateOrderRequest request);

    // DÜZELTME: {id} parametresini metoda ekledik
    @GET("/api/orders/{id}")
    Call<OrderResponse> getOrderID(@Path("id") Long id);

    @GET("/api/staff/tables")
    Call<List<RestaurantTable>> getTables();

    // DÜZELTME: Başındaki @GET eksikti, onu ekledik.
    // Backend'i /api/menu yaptık, burası da /api/menu olmalı.
    @GET("/api/menu")
    Call<List<MenuItem>> getActiveMenu();
}