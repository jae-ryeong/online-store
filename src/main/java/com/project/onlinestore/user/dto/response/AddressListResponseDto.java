package com.project.onlinestore.user.dto.response;

import com.project.onlinestore.user.entity.Address;

public record AddressListResponseDto(
        Long addressId,
        String addresseeName,
        String address,
        String detailAddress,
        Integer postalCode,
        String tel
) {
    public static AddressListResponseDto fromEntity(Address address) {
        return new AddressListResponseDto(address.getId(), address.getAddresseeName(), address.getAddress(), address.getDetailAddress(), address.getPostalCode(), address.getTel());
    }
}
