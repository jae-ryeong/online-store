package com.project.onlinestore.order.dto.request;

import java.math.BigDecimal;

public record createOrderRequestDto(
        BigDecimal amount,
        String itemName
) {
}
