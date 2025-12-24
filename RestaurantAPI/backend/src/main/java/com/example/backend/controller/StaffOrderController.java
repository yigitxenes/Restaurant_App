package com.example.backend.controller;

import com.example.backend.dto.UpdateStatusRequest;
import com.example.backend.entity.Order;
import com.example.backend.entity.RestaurantTable;
import com.example.backend.enums.OrderStatus;
import com.example.backend.repository.RestaurantTableRepository;
import com.example.backend.service.StaffOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staff") // DÜZELTİLDİ: Artık tüm adresler /api/staff ile başlayacak
public class StaffOrderController {

    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    private final StaffOrderService staffOrderService;

    public StaffOrderController(StaffOrderService staffOrderService) {
        this.staffOrderService = staffOrderService;
    }

    // 1. Tüm Masaları Getir
    @GetMapping("/tables")
    public List<RestaurantTable> getAllTables() {
        return restaurantTableRepository.findAll();
    }

    // 2. Bir Masanın Aktif Siparişini Getir (YENİ)
    @GetMapping("/tables/{tableId}/order")
    public ResponseEntity<Order> getTableOrder(@PathVariable Long tableId) {
        Optional<Order> order = staffOrderService.getActiveOrderForTable(tableId);
        return order.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Sipariş Durumunu Güncelle
    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam Long staffId,
            @RequestBody UpdateStatusRequest req
    ) {
        try {
            OrderStatus newStatus = OrderStatus.valueOf(req.getNewStatus());
            Order updatedOrder = staffOrderService.updateOrderStatus(staffId, id, newStatus);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}