package com.project.onlinestore.order.dto.response;

import com.project.onlinestore.order.dto.OrderItemDto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderViewResponseDto(
        Long userId,
        Long orderId,
        LocalDateTime orderDate,
        List<OrderItemDto> orderItemDtoList
) {
}
