package com.example.backend.service;

import com.example.backend.entity.*;
import com.example.backend.enums.OrderStatus;
import com.example.backend.enums.Role;
import com.example.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final UserRepository userRepository;
    private final RestaurantTableRepository tableRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository; // YENİ EKLENDİ

    public OrderService(
            UserRepository userRepository,
            RestaurantTableRepository tableRepository,
            MenuItemRepository menuItemRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository // Constructor'a eklendi
    ) {
        this.userRepository = userRepository;
        this.tableRepository = tableRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public Order createOrder(Long customerId, Long tableId, List<CreateOrderLine> lines) {
        // --- DEBUG LOGLARI ---
        System.out.println("=================================");
        System.out.println("DEBUG: Yeni Sipariş İsteği Geldi!");
        System.out.println("DEBUG: Customer ID: " + customerId);
        System.out.println("DEBUG: Table ID: " + tableId);
        System.out.println("DEBUG: Gelen Ürün Sayısı: " + (lines != null ? lines.size() : "NULL"));
        System.out.println("=================================");

        // 1. LİSTE BOŞ MU KONTROL ET
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("HATA: Sepet boş, sipariş oluşturulamaz!");
        }

        // 2. MÜŞTERİ BUL VEYA OLUŞTUR
        User customer;
        if (customerId != null) {
            customer = userRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Müşteri bulunamadı ID: " + customerId));
        } else {
            String guestEmail = "guest@restaurant.com";
            Optional<User> existingGuest = userRepository.findByEmail(guestEmail);

            if (existingGuest.isPresent()) {
                customer = existingGuest.get();
            } else {
                User newGuest = new User();
                newGuest.setName("Misafir Müşteri");
                newGuest.setEmail(guestEmail);
                newGuest.setPasswordHash("dummy_pass");
                newGuest.setRole(Role.CUSTOMER);
                customer = userRepository.save(newGuest);
            }
        }

        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Masa bulunamadı ID: " + tableId));

        // 3. SIPARIŞ NESNESİNİ OLUŞTUR
        Order order = new Order();
        order.setCustomer(customer);
        order.setTable(table);
        order.setStatus(OrderStatus.RECEIVED);

        // Önce Order'ı kaydet ki bir ID'si olsun (Hibernate bazen ID olmadan Child eklerken hata verebilir)
        Order savedOrder = orderRepository.save(order);

        // 4. ÜRÜNLERİ DÖNGÜYE AL VE EKLE
        for (CreateOrderLine line : lines) {
            MenuItem menuItem = menuItemRepository.findById(line.menuItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Menü ürünü bulunamadı ID: " + line.menuItemId()));

            OrderItem oi = new OrderItem();
            oi.setOrder(savedOrder); // Kaydedilmiş order'ı set et
            oi.setMenuItem(menuItem);
            oi.setQuantity(line.quantity());
            oi.setUnitPrice(menuItem.getPrice());

            // OrderItem'ı listeye ekle (Java tarafı için)
            savedOrder.getItems().add(oi);

            // GARANTİ YÖNTEM: OrderItem'ı veritabanına doğrudan kaydet
            orderItemRepository.save(oi);
        }

        System.out.println("DEBUG: Sipariş ve " + lines.size() + " adet ürün başarıyla veritabanına yazıldı.");
        return savedOrder;
    }

    @Transactional(readOnly = true)
    public Order getOrder(Long orderId) {
        return orderRepository.findDetailedById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Sipariş bulunamadı ID: " + orderId));
    }

    public Optional<Order> getActiveOrderForTable(Long tableId) {
        return orderRepository.findActiveOrderWithItems(tableId, OrderStatus.DELIVERED);
    }
}