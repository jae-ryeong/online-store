package com.project.onlinestore.order.service;

import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.repository.ItemRepository;
import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.exception.ErrorCode;
import com.project.onlinestore.order.Entity.Order;
import com.project.onlinestore.order.Entity.OrderItem;
import com.project.onlinestore.order.Entity.enums.OrderStatus;
import com.project.onlinestore.order.dto.OrderItemDto;
import com.project.onlinestore.order.dto.response.OrderResponseDto;
import com.project.onlinestore.order.dto.response.OrderViewResponseDto;
import com.project.onlinestore.order.repository.OrderItemRepository;
import com.project.onlinestore.order.repository.OrderRepository;
import com.project.onlinestore.user.entity.ItemCart;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.repository.ItemCartRepository;
import com.project.onlinestore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemCartRepository itemCartRepository;

// TODO: 서비스코드 ( 주문하기, 취소하기, 주문하면 아이템의 재고 체크하기, 재고 없으면 soldout만들기 등등 )
    /*
    장바구니에 상품들을 담아놓는다 -> 주문할 상품들을 check -> 주문버튼
     */
    @Transactional
    public OrderResponseDto itemOrder(String userName) {
        User user = findUser(userName);
        List<ItemCart> itemCarts = itemCartRepository.findAllCheckedCart(user.getCart());

        Order order = orderRepository.save(Order.builder().user(user).orderStatus(OrderStatus.ORDER).build());

        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        int totalPrice = 0;

        for (ItemCart i : itemCarts) {
            if(i.getItem().getQuantity() < i.getQuantity()){
                throw new ApplicationException(ErrorCode.NOT_ENOUGH_QUANTITY, i.getItem().getItemName()); // 한 상품 이라도 재고가 부족할 시 에러 발생
            }

            OrderItem orderItem = orderItemRepository.save(OrderItem.builder()
                    .item(i.getItem())
                    .order(order)
                    .orderPrice(i.getQuantity() * i.getItem().getPrice())
                    .count(i.getQuantity())
                    .build());

            orderItemDtoList.add(new OrderItemDto(
                    orderItem.getItem().getId(),
                    orderItem.getItem().getItemName(),
                    orderItem.getCount(),
                    orderItem.getOrderPrice()));

            totalPrice += i.getQuantity() * i.getItem().getPrice();
            itemCartRepository.deleteById(i .getId());   // 주문했으므로 주문한 상품들은 장바구니에서 삭제
            itemCartRepository.flush(); // delete이후 insert가 있는 경우 delete문 실행 X, 그러므로 flush를 해줘야 한다.
            itemRepository.itemCountAndQuantityUpdate(orderItem.getCount(), orderItem.getItem().getId());    // 주문한 상품의 판매 횟수 증가 및 재고 감소
        }

        return new OrderResponseDto(user.getId(), order.getOrderStatus(), totalPrice, orderItemDtoList);
    }

    public List<OrderViewResponseDto> orderView(String userName) {
        User user = findUser(userName);
        Order order = findOrder(user.getId());    // TODO: 에러 고치기 및 테스트코드 작성
        List<OrderItem> orderItems = order.getOrderItems();
        List<OrderViewResponseDto> dtoList = new ArrayList<>();

        for (OrderItem orderItem : orderItems) {
            List<OrderItemDto> orderItemDtoList = new ArrayList<>();

            orderItemDtoList.add(new OrderItemDto(
                    orderItem.getItem().getId(),
                    orderItem.getItem().getItemName(),
                    orderItem.getCount(),
                    orderItem.getOrderPrice()));

            dtoList.add(new OrderViewResponseDto(user.getId(), order.getId(), order.getOrderStatus(), order.getOrderDate(), orderItemDtoList));
        }

        return dtoList;
    }

    private User findUser(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() ->
                new ApplicationException(ErrorCode.USERNAME_NOT_FOUND, userName + "를 찾을 수 없습니다."));
    }
    private Item findItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new ApplicationException(ErrorCode.ITEM_ID_NOT_FOUND, itemId + "를 찾을 수 없습니다."));
    }

    private Order findOrder(Long userId) {
        return orderRepository.findByUser_Id(userId).orElseThrow(() ->
                new ApplicationException(ErrorCode.ORDER_NOT_FOUND, null));
    }
}
