package com.example.restaurantApp.entity;
public class RestaurantTable {

    private Long id;
    private Integer tableNumber;
    private String qrCodeValue;

    // Boş Constructor (Gerekli)
    public RestaurantTable() {}

    public RestaurantTable(Long id, Integer tableNumber, String qrCodeValue) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.qrCodeValue = qrCodeValue;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getTableNumber() { return tableNumber; }
    public void setTableNumber(Integer tableNumber) { this.tableNumber = tableNumber; }

    public String getQrCodeValue() { return qrCodeValue; }
    public void setQrCo