package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurant_tables")
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Table number visible to customers/staff (e.g., 5).
    @Column(name = "table_number", nullable = false)
    private Integer tableNumber;

    // QR code payload stored for lookup (e.g., "tableId=5").
    @Column(name = "qr_code_value", nullable = false)
    private String qrCodeValue;

    // Required by JPA.
    public RestaurantTable() {}

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Integer getTableNumber() { return tableNumber; }

    public void setTableNumber(Integer tableNumber) { this.tableNumber = tableNumber; }

    public String getQrCodeValue() { return qrCodeValue; }

    public void setQrCodeValue(String qrCodeValue) { this.qrCodeValue = qrCodeValue; }
}
