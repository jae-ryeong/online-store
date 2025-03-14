package com.project.onlinestore.order.dto.response;

import java.math.BigDecimal;

public record CreateOrderResponseDto(
        Long orderId,
        //String itemName,
        BigDecimal amount,
        Boolean paymentStatus
        //List<OrderItemDto> orderItems
) {
}
