package com.project.onlinestore.order.service;

import com.project.onlinestore.Item.repository.ItemRepository;
import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.exception.ErrorCode;
import com.project.onlinestore.order.Entity.Order;
import com.project.onlinestore.order.Entity.OrderItem;
import com.project.onlinestore.order.Entity.enums.OrderStatus;
import com.project.onlinestore.order.dto.OrderAddressDto;
import com.project.onlinestore.order.dto.OrderItemDto;
import com.project.onlinestore.order.dto.OrderItemStatusDto;
import com.project.onlinestore.order.dto.request.OrderAddressRequestDto;
import com.project.onlinestore.order.dto.response.OrderDetailViewResponseDto;
import com.project.onlinestore.order.dto.response.OrderResponseDto;
import com.project.onlinestore.order.dto.response.OrderViewResponseDto;
import com.project.onlinestore.order.repository.OrderItemRepository;
import com.project.onlinestore.order.repository.OrderRepository;
import com.project.onlinestore.user.entity.Address;
import com.project.onlinestore.user.entity.ItemCart;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.repository.AddressRepository;
import com.project.onlinestore.user.repository.ItemCartRepository;
import com.project.onlinestore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final AddressRepository addressRepository;

    //장바구니에 상품들을 담아놓는다 -> 주문할 상품들을 check -> 주문버튼
    @Transactional
    public OrderResponseDto itemOrder(String userName, OrderAddressRequestDto dto) {
        User user = findUser(userName);
        Address address = findAddress(dto.addressId());

        if (address.getUser() != user) {
            throw new ApplicationException(ErrorCode.INVALID_USER, null);
        }

        List<ItemCart> itemCarts = itemCartRepository.findAllCheckedCart(user.getCart());
        OrderAddressDto addressDto = new OrderAddressDto(address.getAddresseeName(), address.getAddress(), address.getDetailAddress(), address.getPostalCode(), address.getTel());

        Order order = orderRepository.save(Order.builder().user(user).address(address).build());

        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        int totalPrice = 0;

        for (ItemCart i : itemCarts) {
            if (i.getItem().getQuantity() < i.getQuantity()) {
                throw new ApplicationException(ErrorCode.NOT_ENOUGH_QUANTITY, i.getItem().getItemName()); // 한 상품 이라도 재고가 부족할 시 에러 발생
            }

            OrderItem orderItem = orderItemRepository.save(OrderItem.builder()
                    .item(i.getItem())
                    .order(order)
                    .orderPrice(i.getQuantity() * i.getItem().getPrice())
                    .count(i.getQuantity())
                    .orderStatus(OrderStatus.ORDER)
                    .build());

            orderItemDtoList.add(new OrderItemDto(
                    orderItem.getItem().getId(),
                    orderItem.getItem().getItemName(),
                    orderItem.getCount(),
                    orderItem.getOrderPrice(),
                    orderItem.getOrderStatus()));

            totalPrice += i.getQuantity() * i.getItem().getPrice();
            itemCartRepository.deleteById(i.getId());   // 주문했으므로 주문한 상품들은 장바구니에서 삭제
            itemCartRepository.flush(); // delete이후 insert가 있는 경우 delete문 실행 X, 그러므로 flush를 해줘야 한다.
            itemRepository.itemCountAndQuantityUpdate(orderItem.getCount(), orderItem.getItem().getId());    // 주문한 상품의 판매 횟수 증가 및 재고 감소
        }
        return new OrderResponseDto(user.getId(), totalPrice, orderItemDtoList, addressDto);
    }

    public List<OrderViewResponseDto> orderView(String userName) {
        User user = findUser(userName);
        List<Order> orderList = orderRepository.findAllByUser_Id(user.getId());

        List<OrderViewResponseDto> dtoList = new ArrayList<>();

        for (Order order : orderList) {
            List<OrderItem> orderItems = orderItemRepository.findAllByOrder_Id(order.getId());
            List<OrderItemDto> orderItemDtoList = new ArrayList<>();
            for (OrderItem orderItem : orderItems) {
                orderItemDtoList.add(new OrderItemDto(
                        orderItem.getItem().getId(),
                        orderItem.getItem().getItemName(),
                        orderItem.getCount(),
                        orderItem.getOrderPrice(),
                        orderItem.getOrderStatus()));
            }
            dtoList.add(new OrderViewResponseDto(user.getId(), order.getId(), order.getOrderDate(), orderItemDtoList));
        }

        return dtoList;
    }

    public OrderDetailViewResponseDto orderDetailView(Long orderId) {
        Order order = findOrder(orderId);

        List<OrderItem> orderItems = orderItemRepository.findAllByOrder_Id(order.getId());

        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        Integer totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getOrderPrice();
            orderItemDtoList.add(new OrderItemDto(orderItem.getItem().getId(), orderItem.getItem().getItemName(), orderItem.getCount(), orderItem.getOrderPrice(), orderItem.getOrderStatus()));
        }

        OrderAddressDto orderAddressDto = new OrderAddressDto(order.getAddress().getAddresseeName(), order.getAddress().getAddress(), order.getAddress().getDetailAddress(), order.getAddress().getPostalCode(), order.getAddress().getTel());

        return new OrderDetailViewResponseDto(order.getOrderDate(), orderItemDtoList, orderAddressDto, totalPrice);
    }

    @Transactional
    public List<OrderItemStatusDto> orderAllCancel(String userName, Long orderId) {
        User user = findUser(userName);
        Order order = findOrder(orderId);
        List<OrderItem> orderItemList = orderItemRepository.findAllByOrder_Id(orderId);

        List<OrderItemStatusDto> orderItemStatusDtoList = new ArrayList<>();

        if (order.getUser() != user) {
            throw new ApplicationException(ErrorCode.INVALID_USER, null);
        }

        for (OrderItem orderItem : orderItemList){  // 모든 상품이 배송 시작 전 단계에서만 주문내역 전체를 cancel 가능
            if (orderItem.getOrderStatus() != OrderStatus.ORDER){
                throw new ApplicationException(ErrorCode.CAN_NOT_CANCELED, null);
            }

            Integer count = orderItem.getCount();
            itemRepository.itemCountAndQuantityUpdate(count * -1, orderItem.getItem().getId());    // count는 내리고, quantity의 갯수는 올려줘야 하기 때문에 *-1
            orderItemRepository.updateOrderStatus(OrderStatus.CANCEL, orderItem.getId());

            orderItemStatusDtoList.add(new OrderItemStatusDto(orderId, orderItem.getId(), OrderStatus.CANCEL));
        }

        return orderItemStatusDtoList;
    }

    @Transactional
    public OrderItemStatusDto orderTakeBack(String userName, Long orderId, Long orderItemId) {
        User user = findUser(userName);
        Order order = findOrder(orderId);
        OrderItem orderItem = findOrderItem(orderItemId);

        if (order.getUser() != user) {
            throw new ApplicationException(ErrorCode.INVALID_USER, null);
        }

        if (orderItem.getOrderStatus() != OrderStatus.COMPLETE || !LocalDateTime.now().isBefore(order.getOrderDate().plusDays(15))) {   // 배송이 완료된 상품만 반품 신청 가능, 주문 후 15일 이내에만 반품 신청 가능
            throw new ApplicationException(ErrorCode.CAN_NOT_TAKE_BACK, null);
        }

        orderItemRepository.updateOrderStatus(OrderStatus.TAKE_BACK_APPLICATION, orderItemId);

        return new OrderItemStatusDto(orderId, orderItemId, OrderStatus.TAKE_BACK_APPLICATION);
    }

    @Transactional
    public OrderItemStatusDto takeBackCompleted(String userName, Long orderItemId) {    // 반품 확정 (판매자가)
        User seller = findUser(userName);
        OrderItem orderItem = findOrderItem(orderItemId);

        if (seller != orderItem.getItem().getUser()){
            throw new ApplicationException(ErrorCode.INVALID_USER, null);
        }
        if(!orderItem.getOrderStatus().equals(OrderStatus.TAKE_BACK_APPLICATION)) { // 반품 신청하지 않은 상품을 반품완료 상태로 변경 시도시 에러
            throw new ApplicationException(ErrorCode.NOT_TAKE_BACK_APPLICATION, null);
        }

        orderItemRepository.updateOrderStatus(OrderStatus.TAKE_BACK_COMPLETE, orderItemId);

        return new OrderItemStatusDto(orderItem.getOrder().getId(), orderItemId, OrderStatus.TAKE_BACK_COMPLETE);
    }

    @Transactional
    public OrderItemStatusDto orderCompleted(String userName, Long orderItemId) {    // 구매 확정 (구매자가)
        User customer = findUser(userName);
        OrderItem orderItem = findOrderItem(orderItemId);

        if (customer != orderItem.getOrder().getUser()){
            throw new ApplicationException(ErrorCode.INVALID_USER, null);
        }
        if(!orderItem.getOrderStatus().equals(OrderStatus.ORDER)) { // Order상태가 아닌 상품을 구매 확정 상태로 변경 시도시 에러
            throw new ApplicationException(ErrorCode.NOT_ORDER_STATUS, null);
        }

        orderItemRepository.updateOrderStatus(OrderStatus.COMPLETE, orderItemId);

        return new OrderItemStatusDto(orderItem.getOrder().getId(), orderItemId, OrderStatus.COMPLETE);
    }


    private User findUser(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() ->
                new ApplicationException(ErrorCode.USERNAME_NOT_FOUND, userName + "를 찾을 수 없습니다."));
    }

    private Order findOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ApplicationException(ErrorCode.ORDER_NOT_FOUND, "를 찾을 수 없습니다."));
        return order;
    }

    private Address findAddress(Long addressId) {
        return addressRepository.findById(addressId).orElseThrow(() ->
                new ApplicationException(ErrorCode.ADDRESS_NOT_FOUNT, null));
    }

    private OrderItem findOrderItem(Long OrderItemId) {
        return orderItemRepository.findById(OrderItemId).orElseThrow(() ->
                new ApplicationException(ErrorCode.ORDER_ITEM_NOT_FOUNT_IN_ORDER, null));
    }
}
