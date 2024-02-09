package com.project.onlinestore.user.dto.request;

public record SellerRequestDto(
        String userName,
        String password,
        String storeName
) {
}
