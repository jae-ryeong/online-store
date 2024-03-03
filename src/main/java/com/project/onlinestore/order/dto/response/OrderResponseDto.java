package com.project.onlinestore.order.dto.response;

import com.project.onlinestore.order.Entity.enums.OrderStatus;
import com.project.onlinestore.order.dto.OrderItemDto;

import java.util.List;

public record OrderResponseDto(
        Long userId,
        OrderStatus orderStatus,
        int totalPrice,
        List<OrderItemDto> orderItemDtoList
) {

}
