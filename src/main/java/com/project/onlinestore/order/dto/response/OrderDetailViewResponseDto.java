package com.project.onlinestore.order.dto.response;

import com.project.onlinestore.order.dto.OrderAddressDto;
import com.project.onlinestore.order.dto.OrderItemDto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailViewResponseDto(
        LocalDateTime orderDate,
        List<OrderItemDto> orderItemDtoList,
        OrderAddressDto addressDto,
        Integer totalPrice
) {
}
