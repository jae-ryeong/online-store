package com.project.onlinestore.order.controller;

import com.project.onlinestore.Item.service.ItemService;
import com.project.onlinestore.order.dto.request.createOrderRequestDto;
import com.project.onlinestore.order.dto.request.OrderAddressRequestDto;
import com.project.onlinestore.order.dto.OrderItemStatusDto;
import com.project.onlinestore.order.dto.response.*;
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
    private final ItemService itemService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDto> orderCheckOut(Authentication authentication, @RequestBody OrderAddressRequestDto dto) {
        OrderResponseDto orderResponseDto = orderService.itemOrder(authentication.getName(), dto);
        orderResponseDto.orderItemDtoList().forEach(orderItemDto -> itemService.itemSoldOutCheck(orderItemDto.itemId(), orderItemDto.orderItemCount()));   //주문 후 아이템 재고 체크 후 soldOut

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

    @PutMapping("/list/{orderId}/cancel") // 주문 취소
    public ResponseEntity<List<OrderItemStatusDto>> orderCancel(Authentication authentication, @PathVariable("orderId") Long orderId) {
        List<OrderItemStatusDto> orderItemStatusDtoList = orderService.orderAllCancel(authentication.getName(), orderId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(orderItemStatusDtoList);
    }

    @PutMapping("/list/{orderId}/{orderItemId}/takeback") // 반품 신청
    public ResponseEntity<OrderItemStatusDto> orderTakeBack(Authentication authentication, @PathVariable("orderId") Long orderId, @PathVariable("orderItemId")Long orderItemId) {
        OrderItemStatusDto orderItemStatusDto = orderService.orderTakeBack(authentication.getName(), orderId, orderItemId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(orderItemStatusDto);
    }

    @PutMapping("/list/{orderItemId}/takeback/completed") // 반품 확정
    public ResponseEntity<OrderItemStatusDto> orderTakeBackCompleted(Authentication authentication, @PathVariable("orderItemId")Long orderItemId) {
        OrderItemStatusDto orderItemStatusDto = orderService.takeBackCompleted(authentication.getName(), orderItemId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(orderItemStatusDto);
    }

    @PutMapping("/list/{orderItemId}/completed") // 배송 확정
    public ResponseEntity<OrderItemStatusDto> orderCompleted(Authentication authentication, @PathVariable("orderItemId")Long orderItemId) {
        OrderItemStatusDto orderItemStatusDto = orderService.orderCompleted(authentication.getName(), orderItemId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(orderItemStatusDto);
    }

    @PostMapping("/createorder")
    public ResponseEntity<CreateOrderResponseDto> createOrder(Authentication authentication, @RequestBody createOrderRequestDto dto){
        System.out.println("dto: " + dto);
        CreateOrderResponseDto createOrderResponseDto = orderService.createOrder(authentication.getName(), dto);

        System.out.println("createOrderResponseDto = " + createOrderResponseDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(createOrderResponseDto);
    }

    @PostMapping("/list/orderitem")
    public ResponseEntity<List<OrderItemResponseDto>> orderItemList(Authentication authentication) {
        List<OrderItemResponseDto> orderItemResponseDtos = orderService.orderitemList(authentication.getName());

        return ResponseEntity.status(HttpStatus.OK)
                .body(orderItemResponseDtos);
    }

    @DeleteMapping("/deleteorder")
    public ResponseEntity<String> orderDelete(Authentication authentication, @RequestBody Long orderId) {
        System.out.println("orderId = " + orderId);
        orderService.cancelOrder(authentication.getName(), orderId);

        return ResponseEntity.ok("결제 실패로 인해 order 데이터가 삭제되었습니다");
    }
}
