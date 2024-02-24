package com.project.onlinestore.Item.service;

import com.project.onlinestore.Item.dto.request.ReviewCreateRequestDto;
import com.project.onlinestore.Item.dto.response.ReviewCreateResponseDto;
import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.entity.Review;
import com.project.onlinestore.Item.repository.ItemRepository;
import com.project.onlinestore.Item.repository.LikeRepository;
import com.project.onlinestore.Item.repository.ReviewRepository;
import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.user.entity.Cart;
import com.project.onlinestore.user.entity.ItemCart;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.entity.enums.RoleType;
import com.project.onlinestore.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @DisplayName("리뷰 등록")
    @Test
    void createReviewTest() {
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        given(userRepository.findByUserName(customer.getUserName())).willReturn(Optional.of(customer));
        given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));
        given(reviewRepository.existsByItemAndCreatedBy(any(),any())).willReturn(false);

        //when
        ReviewCreateResponseDto review = reviewService.createReview(customer.getUserName(), new ReviewCreateRequestDto(item.getId(), "리뷰"));

        //then
        assertThat(review.itemId()).isEqualTo(item.getId());
        assertThat(review.content()).isEqualTo("리뷰");
    }

    @DisplayName("유저가 동일한 아이템에 리뷰 등록시 에러 발생")
    @Test
    void createReviewErrorTest() {
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        given(userRepository.findByUserName(any())).willReturn(Optional.of(customer));
        given(itemRepository.findById(any())).willReturn(Optional.of(item));
        given(reviewRepository.existsByItemAndCreatedBy(any(),any())).willReturn(true);

        //then
        assertThatThrownBy(() -> reviewService.createReview(any(), new ReviewCreateRequestDto(item.getId(), "리뷰"))).isInstanceOf(ApplicationException.class);
    }

    @DisplayName("리뷰 삭제")
    @Test
    public void deleteTest() throws Exception{
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        Review review = Review.builder().content("리뷰").item(item).createdBy("customer").build();

        given(userRepository.findByUserName(customer.getUserName())).willReturn(Optional.of(customer));
        given(reviewRepository.findById(item.getId())).willReturn(Optional.of(review));

        //when
        reviewService.deleteReview(customer.getUserName(), review.getId());

        //then
        verify(reviewRepository).deleteById(any());
    }

    @DisplayName("다른 유저가 리뷰 삭제 - 에러")
    @Test
    public void deleteErrorTest() throws Exception{
        //given
        Cart cart1 = createCart();
        User seller = sellerUser(cart1);
        Item item = createItem(seller);

        Cart cart2 = createCart();
        User customer = customerUser(cart2);

        Review review = Review.builder().content("리뷰").item(item).createdBy("seller").build();

        given(userRepository.findByUserName(customer.getUserName())).willReturn(Optional.of(customer));
        given(reviewRepository.findById(item.getId())).willReturn(Optional.of(review));

        //then
        assertThatThrownBy(() -> reviewService.deleteReview(customer.getUserName(), review.getId())).isInstanceOf(ApplicationException.class)
                .hasMessage("Not Authorized USER, 잘못된 유저 접근입니다.");
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
}