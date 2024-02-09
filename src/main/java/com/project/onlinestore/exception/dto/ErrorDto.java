package com.project.onlinestore.exception.dto;

import com.project.onlinestore.exception.ErrorCode;

public record ErrorDto(
        ErrorCode errorCode,
        String message
) {
}
