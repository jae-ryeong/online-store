package com.project.onlinestore.user.dto;

import com.project.onlinestore.user.entity.enums.RoleType;
import lombok.Builder;

@Builder
public record UserDto(
        String userName,
        String password,
        RoleType roleType,
        String storeName) {

}
