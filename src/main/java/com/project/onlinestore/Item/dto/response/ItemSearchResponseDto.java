package com.project.onlinestore.Item.dto.response;

import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.entity.enums.Category;
import lombok.Builder;

@Builder
public record ItemSearchResponseDto(
        String itemName,
        Integer price,
        Long userId,
        String storeName,
        Category category,
        boolean soldOut
) {

    public static ItemSearchResponseDto fromEntity(Item entity) {
        return new ItemSearchResponseDto(
                entity.getItemName(), entity.getPrice(), entity.getUser().getId(),
                entity.getUser().getStoreName(), entity.getCategory(), entity.isSoldOut()
        );
    }
}
