package com.project.onlinestore.Item.dto.response;

public record itemQuantityResponseDto(
        String storeName,
        Long itemId,
        Integer updateQuantity,
        Integer resultQuantity
) {
}
