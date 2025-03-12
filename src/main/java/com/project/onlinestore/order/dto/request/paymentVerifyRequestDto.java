package com.project.onlinestore.order.dto.request;

import java.math.BigDecimal;

public record paymentVerifyRequestDto(
        String paymentId,
        BigDecimal amount,
        Long orderId
) {
}
