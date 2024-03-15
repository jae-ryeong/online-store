package com.project.onlinestore.order.dto.response;

import com.project.onlinestore.order.Entity.enums.OrderStatus;

public record OrderCancelResponseDto(
        Long orderId,
        OrderStatus orderStatus
) {
}
