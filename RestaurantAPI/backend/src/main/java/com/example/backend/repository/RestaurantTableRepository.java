package com.example.backend.repository;

import com.example.backend.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    Optional<RestaurantTable> findByQrCodeValue(String qrCodeValue);
    Optional<RestaurantTable> findByTableNumber(Integer tableNumber);
}
