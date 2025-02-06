package com.project.onlinestore.Item.dto.response;

public record GCPMainImageResponseDto(
        String userName,
        String fileName,
        String fileUrl
) {
    public static GCPMainImageResponseDto builder(String userName, String fileName, String fileUrl) {
        return new GCPMainImageResponseDto(userName, fileName, fileUrl);
    }
}
