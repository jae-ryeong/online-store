package com.project.onlinestore.user.dto.request;

public record LoginRequestDto(
        String userName,
        String password
) {
}
