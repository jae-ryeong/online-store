package com.project.onlinestore.user.dto.request;

public record AddressRegistrationRequestDto(
        String addresseeName,
        String address,
        String detailAddress,
        Integer postalCode,
        String tel
) {
}
