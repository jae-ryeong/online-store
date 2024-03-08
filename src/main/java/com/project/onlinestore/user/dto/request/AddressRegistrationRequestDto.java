package com.project.onlinestore.user.dto.request;

public record AddressRegistrationRequestDto(
    String address,
    String detailAddress,
    Integer postalCode,
    String tel
) {
}
