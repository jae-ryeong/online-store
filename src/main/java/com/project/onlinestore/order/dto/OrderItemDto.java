package com.project.onlinestore.order.dto;

public record OrderItemDto(
        Long itemId,
        String itemName,
        Integer orderItemCount, // 몇 개 구매할 것 인지
        Integer price
) {
}
