package com.project.onlinestore.user.dto.response;

import com.project.onlinestore.user.entity.ItemCart;

public record CartViewResponseDto(
        Long itemCartId,
        String itemName,
        Integer price,
        String storeName,
        Integer quantity,
        boolean cartCheck,
        String mainImageUrl
) {
    public static CartViewResponseDto fromEntity(ItemCart itemCart) {
        return new CartViewResponseDto(itemCart.getId(), itemCart.getItem().getItemName(), itemCart.getItem().getPrice(), itemCart.getItem().getUser().getStoreName(), itemCart.getQuantity(), itemCart.isCartCheck(), itemCart.getItem().getMainImageUrl());
    }
}
