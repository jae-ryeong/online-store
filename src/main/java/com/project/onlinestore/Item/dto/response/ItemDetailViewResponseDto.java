package com.project.onlinestore.Item.dto.response;

import lombok.Builder;

@Builder
public record ItemDetailViewResponseDto(
        String itemName,
        Integer price,
        String mainImageUrl,
        String description,
        String storeName,
        Long soldCount,
        Integer quantity,
        boolean soldOut
) {
}
