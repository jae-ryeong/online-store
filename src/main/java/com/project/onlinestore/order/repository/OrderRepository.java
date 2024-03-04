package com.project.onlinestore.order.repository;

import com.project.onlinestore.order.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUser_Id(Long userId);
}
