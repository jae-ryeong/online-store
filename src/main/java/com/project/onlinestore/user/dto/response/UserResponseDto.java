package com.project.onlinestore.user.dto.response;

import com.project.onlinestore.user.dto.request.CustomerRequestDto;
import com.project.onlinestore.user.dto.request.SellerRequestDto;
import com.project.onlinestore.user.entity.enums.RoleType;

public record UserResponseDto(
        String userName,
        String password,
        RoleType roleType,
        String storeName
) {
    public static UserResponseDto fromCustomerDto(CustomerRequestDto userDto) {
        return new UserResponseDto(
                userDto.userName(), userDto.password(), RoleType.CUSTOMER, null
        );
    }
    public static UserResponseDto fromSellerDto(SellerRequestDto userDto) {
        return new UserResponseDto(
                userDto.userName(), userDto.password(), RoleType.SELLER, userDto.storeName()
        );
    }
}
