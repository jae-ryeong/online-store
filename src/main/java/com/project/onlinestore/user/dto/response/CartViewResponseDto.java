package com.project.onlinestore.user.dto.response;

import com.project.onlinestore.user.entity.ItemCart;

public record CartViewResponseDto(
        String itemName,
        Integer price,
        String storeName,
        boolean cartCheck
) {
    public static CartViewResponseDto fromEntity(ItemCart itemCart) {
        return new CartViewResponseDto(itemCart.getItem().getItemName(), itemCart.getItem().getPrice(), itemCart.getItem().getUser().getStoreName(), itemCart.isCartCheck());
    }
}
