package com.project.onlinestore.dto.request;

public record SellerRequestDto(
        String userName,
        String password,
        String storeName
) {
}
