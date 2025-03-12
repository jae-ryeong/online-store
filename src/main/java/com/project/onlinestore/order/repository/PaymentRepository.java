package com.project.onlinestore.order.repository;

import com.project.onlinestore.order.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
