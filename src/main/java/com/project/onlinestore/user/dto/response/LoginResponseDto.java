package com.project.onlinestore.user.dto.response;

import lombok.Builder;

@Builder
public record LoginResponseDto(
        String userName,
        String accessToken,
        String refreshToken
) {
}
