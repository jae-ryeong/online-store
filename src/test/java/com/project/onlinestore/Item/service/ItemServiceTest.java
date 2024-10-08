package com.project.onlinestore.Item.service;

import com.project.onlinestore.Item.dto.request.RegistrationRequestDto;
import com.project.onlinestore.Item.dto.request.itemQuantityRequestDto;
import com.project.onlinestore.Item.dto.response.RegistrationResponseDto;
import com.project.onlinestore.Item.dto.response.itemQuantityResponseDto;
import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.entity.enums.Category;
import com.project.onlinestore.Item.repository.ItemRepository;
import com.project.onlinestore.Item.repository.LikeRepository;
import com.project.onlinestore.Item.repository.ReviewRepository;
import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.user.entity.Cart;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.entity.enums.RoleType;
import com.project.onlinestore.user.repository.ItemCartRepository;
import com.project.onlinestore.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemCartRepository itemCartRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private ItemService itemService;

    @DisplayName("정상적으로 상품이 등록")
    @Test
    public void registrationTest_OK() throws Exception {
        //given
        Cart cart = createCart();
        User user = sellerUser(cart);
        given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(user));

        //when
        RegistrationResponseDto registration = itemService.registration(user.getUserName(), itemDto());

        //then
        verify(userRepository).findByUserName(any());
        assertThat(registration.itemName()).isEqualTo(itemDto().itemName());
        assertThat(registration.userId()).isEqualTo(user.getId());
    }

    @DisplayName("customerType 상품 등록 시도, 에러 발생")
    @Test
    public void registrationTest_roleType_Error() throws Exception {
        //given
        Cart cart = createCart();
        User user = customerUser(cart);
        given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(user));

        //then
        assertThatThrownBy(() -> itemService.registration(user.getUserName(), itemDto())).isInstanceOf(ApplicationException.class);
    }

    @DisplayName("item 전체 목록 조회")
    @Test
    public void allItemSearchTest() throws Exception {
        //given
        Pageable pageable = Mockito.mock(Pageable.class);
        given(itemRepository.findAll(pageable)).willReturn(Page.empty());

        //then
        assertThatCode(() -> itemService.findAllItem(pageable)).doesNotThrowAnyException();
    }
    
    @DisplayName("아이템 삭제")
    @Test
    public void deleteTest() throws Exception{
        //given
        Cart cart = createCart();
        User seller = sellerUser(cart);
        Item item = createItem(seller);

        given(userRepository.findByUserName(seller.getUserName())).willReturn(Optional.of(seller));
        given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));

        //when
        itemService.deleteItem(seller.getUserName(), item.getId());
        
        //then
        verify(itemRepository).deleteById(any());
    }

    @DisplayName("아이템 삭제시 장바구니 속 아이템들도 삭제")
    @Test
    public void deleteItemCartTest() throws Exception{
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);

        given(userRepository.findByUserName(seller.getUserName())).willReturn(Optional.of(seller));
        given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));

        //when
        itemService.deleteItem(seller.getUserName(), item.getId());

        //then
        verify(itemCartRepository).deleteAllByItem(item);
    }

    @DisplayName("아이템 삭제시 아이템 리뷰들도 삭제")
    @Test
    public void deleteReviewTest() throws Exception{
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);

        given(userRepository.findByUserName(seller.getUserName())).willReturn(Optional.of(seller));
        given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));

        //when
        itemService.deleteItem(seller.getUserName(), item.getId());

        //then
        verify(reviewRepository).deleteAllByItem(item);
    }

    @DisplayName("아이템 삭제시 아이템 좋아요들도 삭제")
    @Test
    public void deleteLikeTest() throws Exception{
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);

        given(userRepository.findByUserName(seller.getUserName())).willReturn(Optional.of(seller));
        given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));

        //when
        itemService.deleteItem(seller.getUserName(), item.getId());

        //then
        verify(likeRepository).deleteAllByItem_Id(item.getId());
    }

    @DisplayName("아이템 삭제시 다른 Seller의 아이템 삭제 - 에러 발생")
    @Test
    public void deleteItemCartErrorTest() throws Exception{
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);
        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        given(userRepository.findByUserName(any())).willReturn(Optional.of(customer));
        given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));

        //then
        assertThatThrownBy(() -> itemService.deleteItem(any(), item.getId())).isInstanceOf(ApplicationException.class);
    }

    @DisplayName("재고가 0이하인 제품의 isSoldOut을 True로 업데이트한다.")
    @Test
    public void itemSoldOutCheckTest() throws Exception{
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);

        given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));

        //when
        itemService.itemSoldOutCheck(item.getId(), 100);

        //then
        verify(itemRepository).itemSoldOutTrue(any());
    }

    @DisplayName("판매자가 상품의 재고 변경")
    @Test
    public void itemQuantityUpdateTest() throws Exception{
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);

        given(userRepository.findByUserName(seller.getUserName())).willReturn(Optional.of(seller));
        given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));

        //when
        itemQuantityResponseDto result = itemService.itemQuantityUpdate(seller.getUserName(), item.getId(), new itemQuantityRequestDto(1));

        //then
        verify(itemRepository).itemQuantityUpdate(1, item.getId());
        assertThat(result.resultQuantity()).isEqualTo(101);
    }

    @DisplayName("상품의 재고 변경시 soldOut도 변경")
    @Test
    public void itemQuantityUpdateSoldOutTest() throws Exception{
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);

        given(userRepository.findByUserName(seller.getUserName())).willReturn(Optional.of(seller));
        given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));

        //when
        itemQuantityResponseDto result = itemService.itemQuantityUpdate(seller.getUserName(), item.getId(), new itemQuantityRequestDto(-100));

        //then
        verify(itemRepository).itemSoldOutTrue(item.getId());
        verify(itemRepository).itemQuantityUpdate(-100, item.getId());
        assertThat(result.resultQuantity()).isEqualTo(0);
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

    private RegistrationRequestDto itemDto() {
        return new RegistrationRequestDto("item", 100, 10000, Category.PET);
    }
}