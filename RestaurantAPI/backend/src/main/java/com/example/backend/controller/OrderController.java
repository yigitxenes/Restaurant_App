package com.example.backend.controller;

import com.example.backend.dto.CreateOrderRequest;
import com.example.backend.entity.Order;
import com.example.backend.service.CreateOrderLine;
import com.example.backend.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestBody CreateOrderRequest req) {
        List<CreateOrderLine> lines = req.getItems().stream()
                .map(i -> new CreateOrderLine(i.getMenuItemId(), i.getQuantity()))
                .toList();

        return orderService.createOrder(req.getCustomerId(), req.getTableId(), lines);
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<Order> getCustomerOrders(@PathVariable Long customerId) {
        return orderService.getCustomerOrders(customerId);
    }

    @GetMapping("/table/{tableId}")
    public List<Order> getTableOrders(@PathVariable Long tableId) {
        return orderService.getTableOrders(tableId);
    }
}
