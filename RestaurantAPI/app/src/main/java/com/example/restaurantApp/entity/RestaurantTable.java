package com.example.restaurantApp.entity;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class RestaurantTable implements Serializable {

    @SerializedName("id")
    private Long id;

    @SerializedName("tableNumber")
    private Integer tableNumber;

    @SerializedName("qrCodeValue")
    private String qrCodeValue;

    // YENİ EKLENEN KISIM
    // Backend'de boolean getter "isOccupied()" olduğu için JSON'a "occupied" olarak gelebilir.
    // Garanti olsun diye alternatif isimleri de destekleyebiliriz ama genelde "occupied" gelir.
    @SerializedName(value = "occupied", alternate = {"isOccupied"})
    private boolean isOccupied;

    public RestaurantTable() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getQrCodeValue() {
        return qrCodeValue;
    }

    public void setQrCodeValue(String qrCodeValue) {
        this.qrCodeValue = qrCodeValue;
    }

    // Yeni Getter ve Setter
    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
}