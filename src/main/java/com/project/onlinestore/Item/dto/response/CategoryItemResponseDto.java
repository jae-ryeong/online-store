package com.project.onlinestore.Item.dto.response;

import lombok.Builder;

@Builder
public record CategoryItemResponseDto(
        String itemName,
        Integer price,
        String mainImageUrl
) {
}
