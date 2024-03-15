package com.project.onlinestore.order.controller;

import com.project.onlinestore.order.dto.request.OrderAddressRequestDto;
import com.project.onlinestore.order.dto.response.OrderCancelResponseDto;
import com.project.onlinestore.order.dto.response.OrderDetailViewResponseDto;
import com.project.onlinestore.order.dto.response.OrderResponseDto;
import com.project.onlinestore.order.dto.response.OrderViewResponseDto;
import com.project.onlinestore.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDto> orderCheckOut(Authentication authentication, @RequestBody OrderAddressRequestDto dto) {
        OrderResponseDto orderResponseDto = orderService.itemOrder(authentication.getName(), dto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(orderResponseDto);
    }

    @GetMapping("/list")
    public ResponseEntity<List<OrderViewResponseDto>> orderList(Authentication authentication) {
        List<OrderViewResponseDto> dtoList = orderService.orderView(authentication.getName());

        return ResponseEntity.status(HttpStatus.OK)
                .body(dtoList);
    }

    @GetMapping("/detail/{orderId}")
    public ResponseEntity<OrderDetailViewResponseDto> orderDetailView(@PathVariable("orderId")Long orderId) {
        OrderDetailViewResponseDto orderDetailView = orderService.orderDetailView(orderId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(orderDetailView);
    }

    @PostMapping("/list/{orderId}/cancel") // 주문 취소
    public ResponseEntity<OrderCancelResponseDto> orderCompleted(Authentication authentication, @PathVariable("orderId") Long orderId) {
        OrderCancelResponseDto orderCancelResponseDto = orderService.orderCancel(authentication.getName(), orderId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(orderCancelResponseDto);
    }

/*    @PostMapping("/list/{orderId}/completed") // 배송 확정
    public ResponseEntity<?> orderCompleted(@PathVariable("orderId") Long orderId) {

    }*/
}
