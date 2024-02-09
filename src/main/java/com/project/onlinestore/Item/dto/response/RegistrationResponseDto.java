package com.project.onlinestore.Item.dto.response;

import lombok.Builder;

@Builder
public record RegistrationResponseDto(
        String itemName,
        Integer quantity,
        Integer price,
        Long userId,
        String storeName
) {
}
