package com.project.onlinestore.Item.dto.response;

public record ItemLikeResponseDto(
        Long itemId,
        Long userId,
        Integer likeCount

) {
}
