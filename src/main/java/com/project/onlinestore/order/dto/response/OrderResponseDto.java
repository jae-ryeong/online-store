package com.project.onlinestore.order.dto.response;

import com.project.onlinestore.order.dto.OrderAddressDto;
import com.project.onlinestore.order.dto.OrderItemDto;

import java.util.List;

public record OrderResponseDto(
        Long userId,
        int totalPrice,
        List<OrderItemDto> orderItemDtoList,
        OrderAddressDto orderAddressDto
) {

}
