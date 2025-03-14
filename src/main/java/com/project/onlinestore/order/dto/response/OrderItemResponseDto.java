package com.project.onlinestore.order.dto.response;

public record OrderItemResponseDto(
        String itemName,
        Integer price,
        Integer quantity
) {
}
