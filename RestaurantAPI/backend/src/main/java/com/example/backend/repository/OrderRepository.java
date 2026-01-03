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
    List<Order> findByStatusNotOrderByCreatedAtAsc(OrderStatus status);

    @EntityGraph(attributePaths = {"customer", "table", "items", "items.menuItem"})
    Optional<Order> findDetailedById(Long id);

    // DÜZELTİLEN KISIM: 'DELIVERED' yerine tam Enum yolu
    @Query("SELECT o FROM Order o WHERE o.table.id = :tableId AND o.status <> com.example.backend.enums.OrderStatus.DELIVERED")
    Optional<Order> findActiveOrderByTableId(@Param("tableId") Long tableId);
}