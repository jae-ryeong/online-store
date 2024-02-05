package com.project.onlinestore.dto.response;

import lombok.Builder;

@Builder
public record LoginResponseDto(
        String userName,
        String password,
        String token
) {
}
