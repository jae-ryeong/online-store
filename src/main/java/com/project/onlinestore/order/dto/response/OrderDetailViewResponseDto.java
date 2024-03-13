package com.project.onlinestore.order.dto.response;

import com.project.onlinestore.order.Entity.enums.OrderStatus;
import com.project.onlinestore.order.dto.OrderAddressDto;
import com.project.onlinestore.order.dto.OrderItemDto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailViewResponseDto(
        OrderStatus orderStatus,
        LocalDateTime orderDate,
        List<OrderItemDto> orderItemDtoList,
        OrderAddressDto addressDto,
        Integer totalPrice
) {
}
