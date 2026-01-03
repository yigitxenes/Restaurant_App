package com.example.backend.controller;

import com.example.backend.dto.UpdateStatusRequest;
import com.example.backend.entity.Order;
import com.example.backend.entity.RestaurantTable;
import com.example.backend.enums.OrderStatus;
import com.example.backend.repository.RestaurantTableRepository;
import com.example.backend.service.StaffOrderService;
// OrderRepository eklendi
import com.example.backend.repository.OrderRepository;
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
    private OrderRepository orderRepository; // Bunu eklemeyi unutma

    private final StaffOrderService staffOrderService;

    public StaffOrderController(StaffOrderService staffOrderService) {
        this.staffOrderService = staffOrderService;
    }

    // GÜNCELLENEN METOD
    @GetMapping("/tables")
    public List<RestaurantTable> getAllTables() {
        List<RestaurantTable> tables = restaurantTableRepository.findAll();

        // Her masa için kontrol et: Aktif sipariş var mı?
        for (RestaurantTable table : tables) {
            boolean hasOrder = orderRepository.findActiveOrderByTableId(table.getId()).isPresent();
            table.setOccupied(hasOrder);
        }
        return tables;
    }

    @GetMapping("/tables/{tableId}/order")
    public ResponseEntity<Order> getTableOrder(@PathVariable Long tableId) {
        Optional<Order> order = staffOrderService.getActiveOrderForTable(tableId);
        return order.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

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