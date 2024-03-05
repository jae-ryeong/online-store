package com.project.onlinestore.order.service;

import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.repository.ItemRepository;
import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.order.Entity.Order;
import com.project.onlinestore.order.Entity.OrderItem;
import com.project.onlinestore.order.Entity.enums.OrderStatus;
import com.project.onlinestore.order.dto.response.OrderResponseDto;
import com.project.onlinestore.order.dto.response.OrderViewResponseDto;
import com.project.onlinestore.order.repository.OrderItemRepository;
import com.project.onlinestore.order.repository.OrderRepository;
import com.project.onlinestore.user.entity.Cart;
import com.project.onlinestore.user.entity.ItemCart;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.entity.enums.RoleType;
import com.project.onlinestore.user.repository.ItemCartRepository;
import com.project.onlinestore.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemCartRepository itemCartRepository;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("상품 주문")
    @Test
    void itemOrderTest() {
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);
        Item item2 = Item.builder()
                .user(seller)
                .itemName("item3").quantity(200).price(30000).build();

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        ItemCart itemCart = createItemCart(cart2, item);
        ItemCart itemCart2 = createItemCart(cart2, item2);

        Order order = Order.builder().user(customer).orderStatus(OrderStatus.ORDER).build();

        OrderItem orderItem = OrderItem.builder().item(item).order(order).count(1).orderPrice(10000).build();

        given(userRepository.findByUserName(customer.getUserName())).willReturn(Optional.of(customer));
        given(itemCartRepository.findAllCheckedCart(cart2)).willReturn(List.of(itemCart, itemCart2));
        given(orderRepository.save(any())).willReturn(order);
        given(orderItemRepository.save(any())).willReturn(orderItem);

        //when
        OrderResponseDto orderResponseDto = orderService.itemOrder(customer.getUserName());

        //then
        verify(itemCartRepository).findAllCheckedCart(cart2);
        verify(itemCartRepository, times(2)).deleteById(any());
        verify(itemRepository, times(2)).itemCountAndQuantityUpdate(any(), any());

        assertThat(orderResponseDto.orderStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(orderResponseDto.orderItemDtoList().size()).isEqualTo(2);
    }

    @DisplayName("상품 주문시 재고가 부족할 때 에러 발생")
    @Test
    void itemOrderNotEnoughQuantityTest() {
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = Item.builder()
                .user(seller)
                .itemName("item").quantity(10).price(20000).build();

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        ItemCart itemCart = createItemCart(cart2, item);

        Order order = Order.builder().user(customer).orderStatus(OrderStatus.ORDER).build();

        given(userRepository.findByUserName(customer.getUserName())).willReturn(Optional.of(customer));
        given(itemCartRepository.findAllCheckedCart(cart2)).willReturn(List.of(itemCart));
        given(orderRepository.save(any())).willReturn(order);

        //then
        assertThatThrownBy(() -> orderService.itemOrder(customer.getUserName())).isInstanceOf(ApplicationException.class)
                .hasMessage("Quantity is not enough, item");
    }

    @DisplayName("주문 목록 조회")
    @Test
    void itemOrderViewTest() {
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);
        Item item2 = Item.builder()
                .user(seller)
                .itemName("item3").quantity(200).price(30000).build();

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        OrderItem orderItem = OrderItem.builder().item(item).count(1).orderPrice(10000).build();
        OrderItem orderItem2 = OrderItem.builder().item(item2).count(1).orderPrice(10000).build();

        Order order = Order.builder().user(customer).orderStatus(OrderStatus.ORDER).orderItems(List.of(orderItem, orderItem2)).build();

        given(userRepository.findByUserName(customer.getUserName())).willReturn(Optional.of(customer));
        given(orderRepository.findByUser_Id(customer.getId())).willReturn(Optional.of(order));

        //when
        List<OrderViewResponseDto> dtoList = orderService.orderView(customer.getUserName());

        //then
        assertThat(dtoList.get(0).orderStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(dtoList.size()).isEqualTo(2);
    }

    private User sellerUser(Cart cart) {
        return User.builder().userName("seller")
                .password("1234")
                .cart(cart)
                .storeName("회사")
                .roleType(RoleType.SELLER)
                .build();
    }

    private User customerUser(Cart cart) {
        return User.builder().userName("customer")
                .password("1234")
                .cart(cart)
                .roleType(RoleType.CUSTOMER)
                .build();
    }

    private Item createItem(User user) {
        return Item.builder()
                .user(user)
                .itemName("item").quantity(100).price(10000).build();
    }

    private Cart createCart() {
        return Cart.builder()
                .build();
    }

    private ItemCart createItemCart(Cart cart, Item item) {
        return ItemCart.builder()
                .cart(cart).item(item).cartCheck(true).quantity(100).build();
    }
}