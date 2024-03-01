package com.project.onlinestore.order.dto.response;

import com.project.onlinestore.order.Entity.enums.OrderStatus;

public record OrderItemResponseDto(
        Long itemId,
        String itemName,
        Integer count,
        Integer price
) {
}
