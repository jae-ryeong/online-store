package com.project.onlinestore.order.controller;

import com.project.onlinestore.order.Entity.Order;
import com.project.onlinestore.order.Entity.OrderItem;
import com.project.onlinestore.order.dto.response.OrderResponseDto;
import com.project.onlinestore.order.dto.response.OrderViewResponseDto;
import com.project.onlinestore.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/list")
    public ResponseEntity<OrderResponseDto> orderList(Authentication authentication) {
        OrderResponseDto orderResponseDto = orderService.itemOrder(authentication.getName());

        return ResponseEntity.status(HttpStatus.OK)
                .body(orderResponseDto);
    }

    @GetMapping("/view")
    public ResponseEntity<List<OrderViewResponseDto>> orderView(Authentication authentication) {
        List<OrderViewResponseDto> dtoList = orderService.orderView(authentication.getName());

        return ResponseEntity.status(HttpStatus.OK)
                .body(dtoList);
    }
}
