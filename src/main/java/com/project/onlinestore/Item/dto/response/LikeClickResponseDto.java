package com.project.onlinestore.Item.dto.response;

public record LikeClickResponseDto(
        Long itemId,
        Long userId,
        Integer likeCount

) {
}
