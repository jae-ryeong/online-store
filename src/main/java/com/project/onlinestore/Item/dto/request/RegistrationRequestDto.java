package com.project.onlinestore.Item.dto.request;


public record RegistrationRequestDto(
        String itemName,
        Integer quantity,
        Integer price
) {
}
