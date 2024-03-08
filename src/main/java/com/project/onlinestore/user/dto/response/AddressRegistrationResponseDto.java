package com.project.onlinestore.user.dto.response;

public record AddressRegistrationResponseDto(
    String address,
    String detailAddress,
    Integer postalCode,
    String tel
) {
}
