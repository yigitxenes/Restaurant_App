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
        System.out.println("DEBUG: Gelen customerId = " + customerId);
        System.out.println("DEBUG: Gelen tableId = " + tableId);

        // --- DEĞİŞİKLİK BURADA BAŞLIYOR ---
        // Eğer ID geldiyse veritabanında ara, gelmediyse (null ise) otomatik MİSAFİR kullan
        User customer;
        if (customerId != null) {
            customer = userRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Müşteri bulunamadı ID: " + customerId));
        } else {
            // ID yoksa 'Misafir' kullanıcısı var mı diye bak, yoksa YARAT.
            String guestEmail = "guest@restaurant.com";
            Optional<User> existingGuest = userRepository.findByEmail(guestEmail);

            if (existingGuest.isPresent()) {
                customer = existingGuest.get();
            } else {
                User newGuest = new User();
                newGuest.setName("Misafir Müşteri");
                newGuest.setEmail(guestEmail);
                newGuest.setPasswordHash("dummy_pass"); // Veritabanı zorunlu tuttuğu için
                newGuest.setRole(Role.CUSTOMER);
                customer = userRepository.save(newGuest);
                System.out.println("✅ Otomatik Misafir Kullanıcı Oluşturuldu: " + customer.getId());
            }
        }
        // --- DEĞİŞİKLİK BURADA BİTİYOR ---

        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Masa bulunamadı ID: " + tableId));

        Order order = new Order();
        order.setCustomer(customer);
        order.setTable(table);
        order.setStatus(OrderStatus.RECEIVED);

        for (CreateOrderLine line : lines) {
            MenuItem menuItem = menuItemRepository.findById(line.menuItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Menü ürünü bulunamadı ID: " + line.menuItemId()));

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
                .orElseThrow(() -> new IllegalArgumentException("Sipariş bulunamadı ID: " + orderId));
    }

    // Personel ekranı için eklediğimiz metod buradaysa kalabilir, silinmediğinden emin ol.
    public Optional<Order> getActiveOrderForTable(Long tableId) {
        return orderRepository.findActiveOrderByTableId(tableId);
    }
}