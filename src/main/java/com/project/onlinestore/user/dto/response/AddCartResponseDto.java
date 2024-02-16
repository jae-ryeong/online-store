package com.project.onlinestore.user.dto.response;

public record AddCartResponseDto(
        Long itemId,
        Long customerId,
        Long sellerId,
        String itemName,
        String storeName
) {
}
