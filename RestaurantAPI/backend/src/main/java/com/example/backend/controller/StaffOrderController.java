package com.example.backend.controller;

import com.example.backend.dto.UpdateStatusRequest;
import com.example.backend.entity.Order;
import com.example.backend.enums.OrderStatus;
import com.example.backend.service.StaffOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.backend.entity.RestaurantTable;
import com.example.backend.repository.RestaurantTableRepository;

import java.util.List;

@RestController
@RequestMapping("/staff/orders")
public class StaffOrderController {
    @Autowired
    private RestaurantTableRepository restaurantTableRepository;
    private final StaffOrderService staffOrderService;

    public StaffOrderController(StaffOrderService staffOrderService) {
        this.staffOrderService = staffOrderService;
    }
    @GetMapping("/api//tables")
    public List<RestaurantTable> getAllTables() {
        return restaurantTableRepository.findAll();
    }

    @PatchMapping("/{id}/status")
    public Order updateStatus(
            @PathVariable Long id,
            @RequestParam Long staffId,
            @RequestBody UpdateStatusRequest req
    ) {
        OrderStatus newStatus = OrderStatus.valueOf(req.getNewStatus());
        return staffOrderService.updateOrderStatus(staffId, id, newStatus);
    }
}
