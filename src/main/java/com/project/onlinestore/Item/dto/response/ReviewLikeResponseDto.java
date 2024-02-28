package com.project.onlinestore.Item.dto.response;

public record ReviewLikeResponseDto(
        Long userId,
        Long reviewId,
        Integer likeCount
) {
}
