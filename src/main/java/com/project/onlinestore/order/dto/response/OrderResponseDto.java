package com.project.onlinestore.order.dto.response;

import com.project.onlinestore.order.Entity.enums.OrderStatus;

import java.util.List;

public record OrderResponseDto(
        Long userId,
        OrderStatus orderStatus,
        List<OrderItemResponseDto> orderItemResponseDtoList
) {

}
