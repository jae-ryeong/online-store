package com.project.onlinestore.Item.dto.response;

import java.time.LocalDateTime;

public record ReviewViewResponseDto(
        String createdBy,
        LocalDateTime createdAt,
        String content,
        Integer likeCount
) {
}
