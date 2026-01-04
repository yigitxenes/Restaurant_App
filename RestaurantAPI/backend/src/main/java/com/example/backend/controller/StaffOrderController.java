package com.example.backend.controller;

import com.example.backend.dto.UpdateStatusRequest;
import com.example.backend.entity.Order;
import com.example.backend.entity.RestaurantTable;
import com.example.backend.enums.OrderStatus;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.RestaurantTableRepository;
import com.example.backend.service.StaffOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staff")
public class StaffOrderController {

    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StaffOrderService staffOrderService;

    // 1. Tüm Masaları Getir
    @GetMapping("/tables")
    public List<RestaurantTable> getAllTables() {
        List<RestaurantTable> tables = restaurantTableRepository.findAll();

        for (RestaurantTable table : tables) {
            boolean isOccupied = orderRepository
                    .findFirstByTableIdAndStatusNot(table.getId(), OrderStatus.DELIVERED)
                    .isPresent();
            table.setOccupied(isOccupied);
        }
        return tables;
    }

    // 2. Bir Masanın Aktif Siparişini Getir
    @GetMapping("/tables/{tableId}/order")
    public ResponseEntity<Order> getTableOrder(@PathVariable Long tableId) {
        // JOIN FETCH ile tek sorguda tüm verileri çek (Order + Items + MenuItems)
        // ORDER BY sayesinde en yeni sipariş ilk sırada gelir
        List<Order> orders = orderRepository.findActiveOrderWithItems(tableId, OrderStatus.DELIVERED);

        // Birden fazla aktif sipariş varsa en yenisini al
        Optional<Order> detailedOrder = orders.stream().findFirst();

        if (detailedOrder.isPresent()) {
            return ResponseEntity.ok(detailedOrder.get());
        }

        return ResponseEntity.notFound().build();
    }

    // 3. Sipariş Durumunu Güncelle
    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam Long staffId,
            @RequestBody UpdateStatusRequest req) {
        try {
            OrderStatus newStatus = OrderStatus.valueOf(req.getNewStatus());
            Order updatedOrder = staffOrderService.updateOrderStatus(staffId, id, newStatus);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}