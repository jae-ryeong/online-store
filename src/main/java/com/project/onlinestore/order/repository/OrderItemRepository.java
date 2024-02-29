package com.project.onlinestore.order.repository;

import com.project.onlinestore.order.Entity.Order;
import com.project.onlinestore.order.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
