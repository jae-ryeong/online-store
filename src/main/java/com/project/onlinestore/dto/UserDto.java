package com.project.onlinestore.dto;

import com.project.onlinestore.entity.User;
import com.project.onlinestore.entity.enums.RoleType;
import lombok.Builder;

@Builder
public record UserDto(
        String userName,
        String password,
        RoleType roleType,
        String storeName) {

}
