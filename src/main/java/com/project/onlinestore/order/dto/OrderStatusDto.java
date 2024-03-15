package com.project.onlinestore.order.dto;

import com.project.onlinestore.order.Entity.enums.OrderStatus;

public record OrderStatusDto(
        Long orderId,
        OrderStatus orderStatus
) {
}
