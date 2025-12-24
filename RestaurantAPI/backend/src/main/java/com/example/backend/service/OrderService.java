package com.example.backend.service;

import com.example.backend.entity.*;
import com.example.backend.enums.OrderStatus;
import com.example.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final UserRepository userRepository;
    private final RestaurantTableRepository tableRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;

    public OrderService(
            UserRepository userRepository,
            RestaurantTableRepository tableRepository,
            MenuItemRepository menuItemRepository,
            OrderRepository orderRepository
    ) {
        this.userRepository = userRepository;
        this.tableRepository = tableRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(Long customerId, Long tableId, List<CreateOrderLine> lines) {
        System.out.println("DEBUG: customerId = " + customerId);
        System.out.println("DEBUG: tableId = " + tableId);
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));

        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Table not found: " + tableId));

        Order order = new Order();
        order.setCustomer(customer);
        order.setTable(table);
        order.setStatus(OrderStatus.RECEIVED);

        for (CreateOrderLine line : lines) {
            MenuItem menuItem = menuItemRepository.findById(line.menuItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + line.menuItemId()));

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setMenuItem(menuItem);
            oi.setQuantity(line.quantity());
            oi.setUnitPrice(menuItem.getPrice());

            order.getItems().add(oi);
        }

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order getOrder(Long orderId) {
    return orderRepository.findDetailedById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }

}
