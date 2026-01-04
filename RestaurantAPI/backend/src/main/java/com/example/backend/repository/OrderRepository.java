package com.example.backend.repository;

import com.example.backend.entity.Order;
import com.example.backend.enums.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    @EntityGraph(attributePaths = { "customer", "table", "items", "items.menuItem" })
    Optional<Order> findDetailedById(Long id);

    Optional<Order> findFirstByTableIdAndStatusNot(Long tableId, OrderStatus status);

    // --- BURAYI DÜZELTTİK: 3 KATMANLI ÇEKİM ---
    // 1. Order'ı getir (Sipariş)
    // 2. FETCH o.items -> İçindeki kalemleri getir (OrderItem)
    // 3. FETCH i.menuItem -> Kalemlerin isim ve fiyatını getir (MenuItem)

    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.items i " + // Bu satır order_items tablosuna gider
            "LEFT JOIN FETCH i.menuItem " + // Bu satır menu_items tablosuna gider (İsim ve Fiyat için)
            "WHERE o.table.id = :tableId " + // Attığın resimdeki table_id sütununa bakar
            "AND o.status <> :status " +
            "ORDER BY o.createdAt DESC") // En yeni siparişi getir
    List<Order> findActiveOrderWithItems(@Param("tableId") Long tableId, @Param("status") OrderStatus status);

    List<Order> findByTableIdOrderByCreatedAtDesc(Long tableId);
}