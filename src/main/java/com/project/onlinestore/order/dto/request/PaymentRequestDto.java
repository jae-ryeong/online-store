package com.project.onlinestore.order.dto.request;

public record PaymentRequestDto(
        String paymentId,
        Long orderId
) {
}
