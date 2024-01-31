package com.project.onlinestore.dto.response;

import com.project.onlinestore.dto.UserDto;
import com.project.onlinestore.dto.request.CustomerRequestDto;
import com.project.onlinestore.dto.request.SellerRequestDto;
import com.project.onlinestore.entity.enums.RoleType;

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
