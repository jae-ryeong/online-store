package com.project.onlinestore.order.dto;

public record OrderAddressDto(
        String addresseeName,
        String address,
        String detailAddress,
        Integer postalCode,
        String tel
) {
}
