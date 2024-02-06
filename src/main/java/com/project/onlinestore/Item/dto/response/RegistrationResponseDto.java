package com.project.onlinestore.Item.dto.response;


public record RegistrationResponseDto(
        String itemName,
        Integer quantity,
        Integer price,
        Long userId,
        String storeName
) {
}
