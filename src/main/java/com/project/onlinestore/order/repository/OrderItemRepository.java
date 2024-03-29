package com.project.onlinestore.order.repository;

import com.project.onlinestore.order.Entity.Order;
import com.project.onlinestore.order.Entity.OrderItem;
import com.project.onlinestore.order.Entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOrder_Id(Long orderId);

    @Query("select exists (select 1 from OrderItem oi where oi.order.id=:orderId and oi.item.id=:itemId)")
    boolean existsByOrderAndItem(@Param("orderId")Long orderId, @Param("itemId")Long itemId);

    @Modifying(clearAutomatically = true)
    @Query("update OrderItem oi set oi.orderStatus = :orderStatus where oi.id = :orderItemId")
    void updateOrderStatus(@Param("orderStatus") OrderStatus orderStatus, @Param("orderItemId")Long orderItemId);

}
