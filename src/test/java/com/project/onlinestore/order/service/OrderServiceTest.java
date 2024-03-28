package com.project.onlinestore.order.service;

import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.repository.ItemRepository;
import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.order.Entity.Order;
import com.project.onlinestore.order.Entity.OrderItem;
import com.project.onlinestore.order.Entity.enums.OrderStatus;
import com.project.onlinestore.order.dto.request.OrderAddressRequestDto;
import com.project.onlinestore.order.dto.OrderItemStatusDto;
import com.project.onlinestore.order.dto.response.OrderDetailViewResponseDto;
import com.project.onlinestore.order.dto.response.OrderResponseDto;
import com.project.onlinestore.order.dto.response.OrderViewResponseDto;
import com.project.onlinestore.order.repository.OrderItemRepository;
import com.project.onlinestore.order.repository.OrderRepository;
import com.project.onlinestore.user.entity.Address;
import com.project.onlinestore.user.entity.Cart;
import com.project.onlinestore.user.entity.ItemCart;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.entity.enums.RoleType;
import com.project.onlinestore.user.repository.AddressRepository;
import com.project.onlinestore.user.repository.ItemCartRepository;
import com.project.onlinestore.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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
    @Mock
    private AddressRepository addressRepository;

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

        Order order = Order.builder().user(customer).build();

        OrderItem orderItem = OrderItem.builder().item(item).order(order).count(1).orderStatus(OrderStatus.ORDER).orderPrice(10000).build();

        Address address = Address.builder().detailAddress("213").address("321").user(customer).tel("010-0000-0000").build();

        given(userRepository.findByUserName(customer.getUserName())).willReturn(Optional.of(customer));
        given(itemCartRepository.findAllCheckedCart(cart2)).willReturn(List.of(itemCart, itemCart2));
        given(orderRepository.save(any())).willReturn(order);
        given(orderItemRepository.save(any())).willReturn(orderItem);
        given(addressRepository.findById(any())).willReturn(Optional.of(address));

        //when
        OrderResponseDto orderResponseDto = orderService.itemOrder(customer.getUserName(), new OrderAddressRequestDto(1L));

        //then
        verify(itemCartRepository).findAllCheckedCart(cart2);
        verify(itemCartRepository, times(2)).deleteById(any());
        verify(itemRepository, times(2)).itemCountAndQuantityUpdate(any(), any());

        assertThat(orderResponseDto.orderItemDtoList().get(0).orderStatus()).isEqualTo(OrderStatus.ORDER);
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

        Order order = Order.builder().user(customer).build();

        Address address = Address.builder().detailAddress("213").address("321").user(customer).tel("010-0000-0000").build();

        given(userRepository.findByUserName(customer.getUserName())).willReturn(Optional.of(customer));
        given(itemCartRepository.findAllCheckedCart(cart2)).willReturn(List.of(itemCart));
        given(orderRepository.save(any())).willReturn(order);
        given(addressRepository.findById(any())).willReturn(Optional.of(address));

        //then
        assertThatThrownBy(() -> orderService.itemOrder(customer.getUserName(), new OrderAddressRequestDto(1L))).isInstanceOf(ApplicationException.class)
                .hasMessage("Quantity is not enough, item");
    }

    @DisplayName("상품 주문시 배송지의 user가 주문자와 다를경우 에러 발생")
    @Test
    void UserNotEqualAddressTest() {
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        Address address = Address.builder().detailAddress("213").address("321").user(seller).tel("010-0000-0000").build();

        given(userRepository.findByUserName(customer.getUserName())).willReturn(Optional.of(customer));
        given(addressRepository.findById(any())).willReturn(Optional.of(address));

        //then
        assertThatThrownBy(() -> orderService.itemOrder(customer.getUserName(), new OrderAddressRequestDto(1L))).isInstanceOf(ApplicationException.class);
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

        OrderItem orderItem = OrderItem.builder().item(item).count(1).orderPrice(10000).orderStatus(OrderStatus.ORDER).build();
        OrderItem orderItem2 = OrderItem.builder().item(item2).count(1).orderPrice(10000).orderStatus(OrderStatus.ORDER).build();

        Order order = Order.builder().user(customer).orderItems(List.of(orderItem, orderItem2)).build();
        Order order2 = Order.builder().user(customer).orderItems(List.of(orderItem, orderItem2)).build();

        given(userRepository.findByUserName(customer.getUserName())).willReturn(Optional.of(customer));
        given(orderRepository.findAllByUser_Id(customer.getId())).willReturn(List.of(order, order2));
        given(orderItemRepository.findAllByOrder_Id(any())).willReturn(List.of(orderItem, orderItem2));

        //when
        List<OrderViewResponseDto> dtoList = orderService.orderView(customer.getUserName());

        //then
        //assertThat(dtoList.get(0).orderStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(dtoList.size()).isEqualTo(2);
        assertThat(dtoList.get(0).orderItemDtoList().get(0).itemName()).isEqualTo("item");
    }

    @DisplayName("주문 상세 조회")
    @Test
    void itemOrderDetailViewTest() {
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);
        Item item2 = Item.builder()
                .user(seller)
                .itemName("item3").quantity(200).price(30000).build();

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        OrderItem orderItem = OrderItem.builder().item(item).count(1).orderPrice(10000).orderStatus(OrderStatus.ORDER).build();
        OrderItem orderItem2 = OrderItem.builder().item(item2).count(1).orderPrice(10000).orderStatus(OrderStatus.ORDER).build();

        Address address = Address.builder().detailAddress("213").address("321").user(customer).tel("010-0000-0000").build();

        Order order = Order.builder().user(customer).orderItems(List.of(orderItem, orderItem2)).address(address).build();

        given(orderRepository.findById(customer.getId())).willReturn(Optional.of(order));
        given(orderItemRepository.findAllByOrder_Id(any())).willReturn(List.of(orderItem, orderItem2));

        //when
        OrderDetailViewResponseDto result = orderService.orderDetailView(order.getId());

        //then
        assertThat(result.orderItemDtoList().size()).isEqualTo(2);
        //assertThat(result.orderStatus()).isEqualTo(OrderStatus.ORDER);
    }

    @DisplayName("주문 취소 테스트")
    @Test
    void itemOrderCancelTest() {
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);
        Item item2 = Item.builder()
                .user(seller)
                .itemName("item3").quantity(200).price(30000).build();

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        OrderItem orderItem = OrderItem.builder().item(item).count(1).orderPrice(10000).orderStatus(OrderStatus.ORDER).build();
        OrderItem orderItem2 = OrderItem.builder().item(item2).count(1).orderPrice(10000).orderStatus(OrderStatus.ORDER).build();

        Address address = Address.builder().detailAddress("213").address("321").user(customer).tel("010-0000-0000").build();

        Order order = Order.builder().user(customer).orderItems(List.of(orderItem, orderItem2)).address(address).build();

        given(userRepository.findByUserName(customer.getUserName())).willReturn(Optional.of(customer));
        given(orderRepository.findById(customer.getId())).willReturn(Optional.of(order));

        //when
        List<OrderItemStatusDto> result = orderService.orderAllCancel(customer.getUserName(), order.getId());

        //then
        verify(orderItemRepository, times(2)).updateOrderStatus(any(), any());
        verify(itemRepository, times(2)).itemCountAndQuantityUpdate(any(), any());
        assertThat(result.get(0).orderStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @DisplayName("일괄적으로 주문 취소시 이미 배송이 시작된 상품이 있어 에러 발생 테스트")
    @Test
    void itemOrderCancelErrorTest() {
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);
        Item item2 = Item.builder()
                .user(seller)
                .itemName("item3").quantity(200).price(30000).build();

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        OrderItem orderItem = OrderItem.builder().item(item).count(1).orderPrice(10000).orderStatus(OrderStatus.ORDER).build();
        OrderItem orderItem2 = OrderItem.builder().item(item2).count(1).orderPrice(10000).orderStatus(OrderStatus.COMPLETE).build();

        Address address = Address.builder().detailAddress("213").address("321").user(customer).tel("010-0000-0000").build();

        Order order = Order.builder().user(customer).orderItems(List.of(orderItem, orderItem2)).address(address).build();

        given(userRepository.findByUserName(customer.getUserName())).willReturn(Optional.of(customer));
        given(orderRepository.findById(customer.getId())).willReturn(Optional.of(order));

        //then
        assertThatThrownBy(() -> orderService.orderAllCancel(customer.getUserName(), order.getId())).isInstanceOf(ApplicationException.class)
                .hasMessage("Orders that have been shipped cannot be canceled.");
    }

    @DisplayName("주문 취소시 주문의 user가 로그인한 유저와 다를경우 에러 발생")
    @Test
    void UserNotEqualOrderUserTest() {
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        Order order = Order.builder().user(seller).build();

        given(userRepository.findByUserName(customer.getUserName())).willReturn(Optional.of(customer));
        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        //then
        assertThatThrownBy(() -> orderService.orderAllCancel(customer.getUserName(), order.getId())).isInstanceOf(ApplicationException.class);
    }

    @DisplayName("배송이 시작된 이후 주문 취소시 취소 불가")
    @Test
    void NotOrderStatus_CanNotCancel_Test() {
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);
        Item item2 = Item.builder()
                .user(seller)
                .itemName("item3").quantity(200).price(30000).build();

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        OrderItem orderItem = OrderItem.builder().item(item).count(1).orderPrice(10000).orderStatus(OrderStatus.ORDER).build();
        OrderItem orderItem2 = OrderItem.builder().item(item2).count(1).orderPrice(10000).orderStatus(OrderStatus.COMPLETE).build();

        Address address = Address.builder().detailAddress("213").address("321").user(customer).tel("010-0000-0000").build();

        Order order = Order.builder().user(customer).orderItems(List.of(orderItem, orderItem2)).address(address).build();

        given(userRepository.findByUserName(customer.getUserName())).willReturn(Optional.of(customer));
        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        //then
        assertThatThrownBy(() -> orderService.orderAllCancel(customer.getUserName(), order.getId())).isInstanceOf(ApplicationException.class)
                .hasMessage("Orders that have been shipped cannot be canceled.");
    }

    @DisplayName("배송이 완료된 이후 반품 신청 정상 동작 테스트")
    @Test
    void orderTakeBackTest() {
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        OrderItem orderItem = OrderItem.builder().item(item).count(1).orderPrice(10000).orderStatus(OrderStatus.COMPLETE).build();

        Order order = Order.builder().user(customer).orderItems(List.of(orderItem)).orderDate(LocalDateTime.now().minusDays(10)).build();

        given(userRepository.findByUserName(customer.getUserName())).willReturn(Optional.of(customer));
        given(orderRepository.findById(any())).willReturn(Optional.of(order));
        given(orderItemRepository.findById(any())).willReturn(Optional.of(orderItem));

        //when
        OrderItemStatusDto result = orderService.orderTakeBack(customer.getUserName(), order.getId(), orderItem.getId());

        //then
        verify(orderItemRepository).updateOrderStatus(OrderStatus.TAKE_BACK_APPLICATION, order.getId());
        assertThat(result.orderStatus()).isEqualTo(OrderStatus.TAKE_BACK_APPLICATION);
    }

    @DisplayName("반품 신청시 주문 목록에 해당 상품이 존재하지 않을 경우 에러 발생")
    @Test
    void orderTakeBackErrorTest() {
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        OrderItem orderItem = OrderItem.builder().item(item).count(1).orderPrice(10000).orderStatus(OrderStatus.ORDER).build();

        Order order = Order.builder().user(customer).orderDate(LocalDateTime.now().minusDays(10)).build();

        given(userRepository.findByUserName(customer.getUserName())).willReturn(Optional.of(customer));
        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        //then
        assertThatThrownBy(() -> orderService.orderTakeBack(customer.getUserName(), order.getId(), orderItem.getId())).isInstanceOf(ApplicationException.class)
                .hasMessage("I couldn't find the item in your order list");
    }

    @DisplayName("배송이 완료되지 않은 상품을 반품 신청시 에러 발생")
    @Test
    void notCompletedOrderTakeBackTest() {
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        OrderItem orderItem = OrderItem.builder().item(item).count(1).orderPrice(10000).orderStatus(OrderStatus.ORDER).build();

        Order order = Order.builder().user(customer).orderDate(LocalDateTime.now().minusDays(10)).build();

        given(userRepository.findByUserName(customer.getUserName())).willReturn(Optional.of(customer));
        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        //then
        assertThatThrownBy(() -> orderService.orderTakeBack(customer.getUserName(), order.getId(), orderItem.getId())).isInstanceOf(ApplicationException.class);
    }

    @DisplayName("주문하고 15일 이후 반품 신청시 에러 발생")
    @Test
    void DaysAfterOrderTakeBackTest() {
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        OrderItem orderItem = OrderItem.builder().item(item).count(1).orderPrice(10000).orderStatus(OrderStatus.ORDER).build();

        Order order = Order.builder().user(customer).orderDate(LocalDateTime.now().minusDays(17)).build();

        given(userRepository.findByUserName(customer.getUserName())).willReturn(Optional.of(customer));
        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        //then
        assertThatThrownBy(() -> orderService.orderTakeBack(customer.getUserName(), order.getId(), orderItem.getId())).isInstanceOf(ApplicationException.class);
    }

    @DisplayName("반품이 완료될 경우 판매자가 반품 완료 상태로 변경")
    @Test
    public void takeBackCompletedTest() throws Exception{
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        Order order = Order.builder().user(customer).orderDate(LocalDateTime.now()).build();

        OrderItem orderItem = OrderItem.builder().item(item).count(1).orderPrice(10000).orderStatus(OrderStatus.TAKE_BACK_APPLICATION).order(order).build();

        given(userRepository.findByUserName(seller.getUserName())).willReturn(Optional.of(seller));
        given(orderItemRepository.findById(any())).willReturn(Optional.of(orderItem));

        //when
        orderService.takeBackCompleted(seller.getUserName(), orderItem.getId());

        //then
        verify(orderItemRepository).updateOrderStatus(OrderStatus.TAKE_BACK_COMPLETE, orderItem.getId());
    }

    @DisplayName("판매자가 자신의 상품이 아닌 상품을 반품 완료 상태로 변경 시도시 에러 발생")
    @Test
    public void takeBackCompleted_NotEqualSeller_Test() throws Exception{
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        Order order = Order.builder().user(customer).orderDate(LocalDateTime.now()).build();

        OrderItem orderItem = OrderItem.builder().item(item).count(1).orderPrice(10000).orderStatus(OrderStatus.TAKE_BACK_APPLICATION).order(order).build();

        given(userRepository.findByUserName(customer.getUserName())).willReturn(Optional.of(customer));
        given(orderItemRepository.findById(any())).willReturn(Optional.of(orderItem));

        //then
        assertThatThrownBy(() -> orderService.takeBackCompleted(customer.getUserName(), orderItem.getId())).isInstanceOf(ApplicationException.class)
                .hasMessage("Not Authorized USER");
    }

    @DisplayName("반품을 요청하지 않은 상품의 상태 변경 시도시 에러 발생")
    @Test
    public void takeBackCompleted_StatusError_Test() throws Exception{
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        Order order = Order.builder().user(customer).orderDate(LocalDateTime.now()).build();

        OrderItem orderItem = OrderItem.builder().item(item).count(1).orderPrice(10000).orderStatus(OrderStatus.ORDER).order(order).build();

        given(userRepository.findByUserName(seller.getUserName())).willReturn(Optional.of(seller));
        given(orderItemRepository.findById(any())).willReturn(Optional.of(orderItem));

        //then
        assertThatThrownBy(() -> orderService.takeBackCompleted(seller.getUserName(), orderItem.getId())).isInstanceOf(ApplicationException.class)
                .hasMessage("This product has not been requested to be returned.");
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