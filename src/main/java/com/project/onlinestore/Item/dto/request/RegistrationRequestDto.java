package com.project.onlinestore.Item.dto.request;

import com.project.onlinestore.Item.entity.enums.Category;

public record RegistrationRequestDto(
        String itemName,
        Integer price,
        Integer quantity,
        Category category,
        String mainImageUrl,
        String description
) {
}
