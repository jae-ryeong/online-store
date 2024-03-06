package com.project.onlinestore.user.dto.response;

public record CartQuantityResponseDto(
        String itemName,
        Integer price,
        String storeName,
        Integer quantity
) {
}
