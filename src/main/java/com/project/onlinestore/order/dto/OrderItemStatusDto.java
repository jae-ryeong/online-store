package com.project.onlinestore.order.dto;

import com.project.onlinestore.order.Entity.enums.OrderStatus;

public record OrderItemStatusDto(
        Long orderId,
        Long orderItem,
        OrderStatus orderStatus
) {
}
