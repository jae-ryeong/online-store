package com.project.onlinestore.Item.dto.response;

import com.project.onlinestore.Item.entity.enums.Category;
import lombok.Builder;

@Builder
public record RegistrationResponseDto(
        String itemName,
        Integer quantity,
        Integer price,
        Long userId,
        String storeName,
        Category category
) {
}
