package com.project.onlinestore.Item.dto.response;

import java.util.List;

public record GCPContentResponseDto(
        String userName,
        String fileName,
        String content,
        List<String> fileUrl) {
}
