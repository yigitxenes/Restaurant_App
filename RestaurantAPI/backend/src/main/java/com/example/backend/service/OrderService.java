package com.example.backend.service;

import com.example.backend.entity.*;
import com.example.backend.enums.OrderStatus;
import com.example.backend.enums.Role;
import com.example.backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final UserRepository userRepository;
    private final RestaurantTableRepository tableRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public OrderService(
            UserRepository userRepository,
            RestaurantTableRepository tableRepository,
            MenuItemRepository menuItemRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository) {
        this.userRepository = userRepository;
        this.tableRepository = tableRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public Order createOrder(Long customerId, Long tableId, List<CreateOrderLine> lines) {
        logger.info("New order request received - Customer ID: {}, Table ID: {}", customerId, tableId);
        logger.debug("Order contains {} items", lines != null ? lines.size() : "NULL");

        // 1. Validate order items
        if (lines == null || lines.isEmpty()) {
            logger.warn("Order creation failed: Empty basket");
            throw new IllegalArgumentException("HATA: Sepet boş, sipariş oluşturulamaz!");
        }

        // 2. Find or create customer
        User customer;
        if (customerId != null) {
            customer = userRepository.findById(customerId)
                    .orElseThrow(() -> {
                        logger.error("Customer not found: {}", customerId);
                        return new IllegalArgumentException("Müşteri bulunamadı ID: " + customerId);
                    });
        } else {
            String guestEmail = "guest@restaurant.com";
            Optional<User> existingGuest = userRepository.findByEmail(guestEmail);

            if (existingGuest.isPresent()) {
                customer = existingGuest.get();
                logger.debug("Using existing guest user");
            } else {
                User newGuest = new User();
                newGuest.setName("Misafir Müşteri");
                newGuest.setEmail(guestEmail);
                newGuest.setPasswordHash(passwordEncoder.encode("dummy_pass"));
                newGuest.setRole(Role.CUSTOMER);
                customer = userRepository.save(newGuest);
                logger.info("Created new guest user with ID: {}", customer.getId());
            }
        }

        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> {
                    logger.error("Table not found: {}", tableId);
                    return new IllegalArgumentException("Masa bulunamadı ID: " + tableId);
                });

        // 3. Create order entity
        Order order = new Order();
        order.setCustomer(customer);
        order.setTable(table);
        order.setStatus(OrderStatus.RECEIVED);

        // Save order first to get ID
        Order savedOrder = orderRepository.save(order);
        logger.debug("Order created with ID: {}", savedOrder.getId());

        // 4. Add order items
        for (CreateOrderLine line : lines) {
            MenuItem menuItem = menuItemRepository.findById(line.menuItemId())
                    .orElseThrow(() -> {
                        logger.error("Menu item not found: {}", line.menuItemId());
                        return new IllegalArgumentException("Menü ürünü bulunamadı ID: " + line.menuItemId());
                    });

            OrderItem oi = new OrderItem();
            oi.setOrder(savedOrder);
            oi.setMenuItem(menuItem);
            oi.setQuantity(line.quantity());
            oi.setUnitPrice(menuItem.getPrice());

            savedOrder.getItems().add(oi);
            orderItemRepository.save(oi);
            logger.debug("Added item {} x{} to order", menuItem.getName(), line.quantity());
        }

        logger.info("Order {} created successfully with {} items", savedOrder.getId(), lines.size());
        return savedOrder;
    }

    @Transactional(readOnly = true)
    public Order getOrder(Long orderId) {
        return orderRepository.findDetailedById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Sipariş bulunamadı ID: " + orderId));
    }

    public Optional<Order> getActiveOrderForTable(Long tableId) {
        return orderRepository.findActiveOrderWithItems(tableId, OrderStatus.DELIVERED)
                .stream().findFirst();
    }

    public List<Order> getCustomerOrders(Long customerId) {
        logger.info("Fetching orders for customer ID: {}", customerId);
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    public List<Order> getTableOrders(Long tableId) {
        logger.info("Fetching active orders for table ID: {}", tableId);
        List<Order> allOrders = orderRepository.findByTableIdOrderByCreatedAtDesc(tableId);
        return allOrders.stream()
                .filter(order -> order.getStatus() != com.example.backend.enums.OrderStatus.DELIVERED)
                .collect(java.util.stream.Collectors.toList());
    }
}