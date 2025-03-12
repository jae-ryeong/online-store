package com.project.onlinestore.order.dto.response;

public record PaymentResponseDto(
        String status,
        Integer amount
) {
}
