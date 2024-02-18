package com.project.onlinestore.user.dto.response;

import com.project.onlinestore.user.entity.ItemCart;

public record CartViewResponseDto(
        String itemName,
        Integer price,
        String sellerName
) {
    public static CartViewResponseDto fromEntity(ItemCart item) {
        return new CartViewResponseDto(item.getItem().getItemName(), item.getItem().getPrice(), item.getItem().getUser().getStoreName());
    }
}
