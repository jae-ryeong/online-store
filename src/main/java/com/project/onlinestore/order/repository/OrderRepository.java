package com.project.onlinestore.order.repository;

import com.project.onlinestore.order.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
