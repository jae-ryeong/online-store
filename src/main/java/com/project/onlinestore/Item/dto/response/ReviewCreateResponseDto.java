package com.project.onlinestore.Item.dto.response;

public record ReviewCreateResponseDto(
        Long itemId,
        Long UserId,
        String content
) {
}
