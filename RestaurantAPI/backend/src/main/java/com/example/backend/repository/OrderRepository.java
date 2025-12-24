package com.example.backend.repository;

import com.example.backend.entity.Order;
import com.example.backend.enums.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    List<Order> findByStatusNotOrderByCreatedAtAsc(OrderStatus status);

    // ✅ JSON döndürürken LAZY patlamasın diye her şeyi tek seferde çekiyoruz
    @EntityGraph(attributePaths = {"customer", "table", "items", "items.menuItem"})
    Optional<Order> findDetailedById(Long id);


    @Query("SELECT o FROM Order o WHERE o.table.id = :tableId AND o.status <> 'DELIVERED'")
    Optional<Order> findActiveOrderByTableId(@Param("tableId") Long tableId);
}
