package com.example.restaurantApp;

import com.example.restaurantApp.dto.CreateOrderRequest;
import com.example.restaurantApp.dto.LoginRequest;
import com.example.restaurantApp.dto.OrderResponse;
import com.example.restaurantApp.dto.UpdateStatusRequest;
import com.example.restaurantApp.entity.MenuItem;
import com.example.restaurantApp.entity.RestaurantTable;
import com.example.restaurantApp.entity.User; // User importu ekledim

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path; // Path importu gerekli
import retrofit2.http.Query;

public interface RestaurantApiService {

    @POST("/api/auth/login")
    Call<User> login(@Body LoginRequest request);

    @POST("/api/orders")
    Call<OrderResponse> placeOrder(@Body CreateOrderRequest request);

    // FIX: Added {id} parameter
    @GET("/api/orders/{id}")
    Call<OrderResponse> getOrderID(@Path("id") Long id);

    @GET("/api/staff/tables")
    Call<List<RestaurantTable>> getTables();

    @GET("/api/staff/tables/{id}/order")
    Call<OrderResponse> getTableActiveOrder(@Path("id") Long tableId);

    // FIX: Added missing @GET annotation
    // Backend uses /api/menu
    @GET("/api/menu")
    Call<List<MenuItem>> getActiveMenu();

    @PATCH("/api/staff/orders/{id}/status")
    Call<OrderResponse> updateOrderStatus(
            @Path("id") Long orderId,
            @Query("staffId") Long staffId,
            @Body UpdateStatusRequest request);

    @GET("/api/orders/customer/{customerId}")
    Call<List<OrderResponse>> getCustomerOrders(@Path("customerId") Long customerId);

    @GET("/api/orders/table/{tableId}")
    Call<List<OrderResponse>> getTableOrders(@Path("tableId") Long tableId);
}