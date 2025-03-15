package com.project.onlinestore.order.repository;

import com.project.onlinestore.order.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser_Id(Long userId);

    //Optional<Order> findByOrderId(Long orderId);
    void deleteById(Long id);

    @Transactional
    @Modifying
    @Query("update Order o set o.paymentStatus = true where o.id=:orderId")
    void changeOrderStatus(@Param("orderId") Long orderId);
}
