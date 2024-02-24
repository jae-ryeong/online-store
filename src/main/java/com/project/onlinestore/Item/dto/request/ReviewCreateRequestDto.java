package com.project.onlinestore.Item.dto.request;

public record ReviewCreateRequestDto(
        Long itemId,
        String content
) {
}
