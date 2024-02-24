package com.project.onlinestore.Item.dto.response;

public record ReviewUpdateResponseDto(
        Long itemId,
        Long userId,
        Long reviewId,
        String updatedContent

) {
}
