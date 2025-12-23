package com.example.backend.service;

import com.example.backend.entity.Order;
import com.example.backend.entity.OrderStatusHistory;
import com.example.backend.entity.User;
import com.example.backend.enums.OrderStatus;
import com.example.backend.enums.Role;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.OrderStatusHistoryRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StaffOrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderStatusHistoryRepository historyRepository;

    public StaffOrderService(
            OrderRepository orderRepository,
            UserRepository userRepository,
            OrderStatusHistoryRepository historyRepository
    ) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
    }

    @Transactional
    public Order updateOrderStatus(Long staffId, Long orderId, OrderStatus newStatus) {

        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff user not found: " + staffId));

        if (staff.getRole() != Role.STAFF) {
            throw new IllegalArgumentException("User is not STAFF: " + staffId);
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        OrderStatus oldStatus = order.getStatus();
        validateTransition(oldStatus, newStatus);

        order.setStatus(newStatus);
        Order saved = orderRepository.save(order);

        OrderStatusHistory h = new OrderStatusHistory();
        h.setOrder(saved);
        h.setOldStatus(oldStatus);
        h.setNewStatus(newStatus);
        h.setChangedBy(staff);
        historyRepository.save(h);

        // ✅ Return a "fully fetched" Order to avoid JSON serialization issues (LAZY proxies).
        return orderRepository.findDetailedById(saved.getId()).orElse(saved);
    }

    private void validateTransition(OrderStatus oldStatus, OrderStatus newStatus) {
        boolean ok =
                (oldStatus == OrderStatus.RECEIVED && newStatus == OrderStatus.PREPARING)
                        || (oldStatus == OrderStatus.PREPARING && newStatus == OrderStatus.READY)
                        || (oldStatus == OrderStatus.READY && newStatus == OrderStatus.DELIVERED);

        if (!ok) {
            throw new IllegalArgumentException("Invalid status transition: " + oldStatus + " -> " + newStatus);
        }
    }
}
