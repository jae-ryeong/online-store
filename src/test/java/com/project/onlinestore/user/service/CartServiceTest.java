package com.project.onlinestore.user.service;

import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.repository.ItemRepository;
import com.project.onlinestore.user.dto.response.AddCartResponseDto;
import com.project.onlinestore.user.entity.Cart;
import com.project.onlinestore.user.entity.ItemCart;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.entity.enums.RoleType;
import com.project.onlinestore.user.repository.CartRepository;
import com.project.onlinestore.user.repository.ItemCartRepository;
import com.project.onlinestore.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemCartRepository itemCartRepository;
    
    @InjectMocks
    private CartService cartService;
    
    @DisplayName("장바구니 버튼 클릭시 장바구니에 추가")
    @Test
    public void addCartTest() throws Exception{
        //given
        User sellerUser = sellerUser();
        Item item = createItem(sellerUser);
        Cart cart = createCart();
        User customerUser = customerUser(cart);

        given(userRepository.findByUserName(customerUser.getUserName())).willReturn(Optional.of(customerUser));
        given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));
        given(cartRepository.findById(any())).willReturn(Optional.of(customerUser.getCart()));

        //when
        AddCartResponseDto addCartResponseDto = cartService.addCart(customerUser.getUserName(), item.getId());

        //then
        verify(itemCartRepository).save(any());
        assertThat(addCartResponseDto.itemId()).isEqualTo(item.getId());
    }

    @DisplayName("장바구니 추가 시 중복된 상품이 있으면 count를 +1")
    @Test
    public void addCartDuplicationTest() throws Exception{
        //given
        User sellerUser = sellerUser();
        Item item = createItem(sellerUser);
        Cart cart = createCart();
        User customerUser = customerUser(cart);
        ItemCart itemCart = createItemCart(cart, item);

        given(userRepository.findByUserName(customerUser.getUserName())).willReturn(Optional.of(customerUser));
        given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));
        given(cartRepository.findById(customerUser.getCart().getId())).willReturn(Optional.of(cart));
        given(itemCartRepository.existsByCartAndItem(cart, item)).willReturn(true);
        given(itemCartRepository.findByCartAndItem(cart, item)).willReturn(itemCart);

        //when
        AddCartResponseDto addCartResponseDto = cartService.addCart(customerUser.getUserName(), item.getId());

        //then
        verify(itemCartRepository).findByCartAndItem(any(),any());
        verify(itemCartRepository).addQuantity(any(),any());
        assertThat(addCartResponseDto.Quantity()).isEqualTo(2);
    }

    @DisplayName("장바구니 목록 전체 조회")
    @Test
    public void allItemCartViewTest() throws Exception{
        //given
        Cart cart = createCart();
        User customerUser = customerUser(cart);

        given(userRepository.findByUserName(customerUser.getUserName())).willReturn(Optional.of(customerUser));

        //when
        cartService.allItemCartView(customerUser.getUserName());

        //then
        verify(itemCartRepository).findAllByCart(any());
    }

    @DisplayName("장바구니 속 체크박스 toggle True -> False")
    @Test
    public void checkToggleTest() throws Exception{
        //given
        User sellerUser = sellerUser();
        Cart cart = createCart();
        User customerUser = customerUser(cart);
        Item item = createItem(sellerUser);
        ItemCart itemCart = createItemCart(cart, item);

        given(userRepository.findByUserName(customerUser.getUserName())).willReturn(Optional.of(customerUser));
        given(itemCartRepository.findById(itemCart.getId())).willReturn(Optional.of(itemCart));

        //when
        cartService.cartCheck(customerUser.getUserName(), itemCart.getId());

        //then
        verify(itemCartRepository).checkFalse(any());
        assertThat(itemCart.isCartCheck()).isFalse();
    }

    private User sellerUser() {
        return User.builder().userName("seller")
                .password("1234")
                .storeName("회사")
                .roleType(RoleType.SELLER)
                .build();
    }

    private User customerUser(Cart cart) {
        return User.builder().userName("seller")
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

    private ItemCart createItemCart(Cart cart, Item item) {
        return ItemCart.builder()
                .cart(cart).item(item).cartCheck(true).quantity(1).build();
    }

    private Cart createCart() {
        return Cart.builder()
                .build();
    }
}