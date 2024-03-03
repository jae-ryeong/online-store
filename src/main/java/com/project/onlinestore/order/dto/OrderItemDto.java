package com.project.onlinestore.order.dto;

public record OrderItemDto(
        Long itemId,
        String itemName,
        Integer count,
        Integer price
) {
}
