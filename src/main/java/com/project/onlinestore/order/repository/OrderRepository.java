package com.project.onlinestore.order.repository;

import com.project.onlinestore.order.Entity.Order;
import com.project.onlinestore.order.Entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser_Id(Long userId);

    @Modifying(clearAutomatically = true)
    @Query("update Order o set o.orderStatus = :cancel where o.id = :orderId")
    void updateOrderStatusCancel(@Param("cancel")OrderStatus cancel, @Param("orderId")Long orderId);

    @Modifying(clearAutomatically = true)
    @Query("update Order o set o.orderStatus = :takeBack where o.id = :orderId")
    void updateOrderStatusTakeBack(@Param("takeBack")OrderStatus takeBack, @Param("orderId")Long orderId);


}
