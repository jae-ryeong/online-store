package com.project.onlinestore.Item.service;

import com.project.onlinestore.Item.dto.response.ReviewLikeResponseDto;
import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.entity.Review;
import com.project.onlinestore.Item.repository.ItemRepository;
import com.project.onlinestore.Item.repository.LikeRepository;
import com.project.onlinestore.Item.repository.ReviewRepository;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.entity.enums.RoleType;
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
class LikeServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private LikeService likeService;

    @DisplayName("item like")
    @Test
    public void itemLikeTest() throws Exception {
        //given
        User user = sellerUser();
        Item item = createItem(user);

        given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(user));
        given(likeRepository.existsByItem_IdAndUser_Id(item.getId(), user.getId())).willReturn(false);
        given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));

        //when
        likeService.itemLike(item.getId(), user.getUserName());

        //then
        verify(likeRepository).save(any());
    }

    @DisplayName("item like, 2번쨰 클릭 (취소)")
    @Test
    public void itemDoubleLikeTest() throws Exception {
        //given
        User user = sellerUser();
        Item item = createItem(user);

        given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(user));
        given(likeRepository.existsByItem_IdAndUser_Id(item.getId(), user.getId())).willReturn(true);

        //when
        likeService.itemLike(item.getId(), user.getUserName());

        //then
        verify(likeRepository).deleteByItem_IdAndUser_Id(any(), any());
    }

    @DisplayName("review like")
    @Test
    public void reviewLikeTest() throws Exception {
        //given
        User user = sellerUser();
        Item item = createItem(user);
        Review review = Review.builder().content("리뷰").item(item).createdBy("customer").build();

        given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(user));
        given(likeRepository.existsByReview_IdAndUser_Id(review.getId(), user.getId())).willReturn(false);
        given(reviewRepository.findById(item.getId())).willReturn(Optional.of(review));

        //when
        ReviewLikeResponseDto reviewLikeResponseDto = likeService.reviewLike(user.getUserName(), review.getId());

        //then
        verify(likeRepository).save(any());

        assertThat(reviewLikeResponseDto.reviewId()).isEqualTo(review.getId());
        assertThat(reviewLikeResponseDto.userId()).isEqualTo(user.getId());
    }

    @DisplayName("review like, 2번쨰 클릭 (취소)")
    @Test
    public void reviewDoubleLikeTest() throws Exception {
        //given
        User user = sellerUser();
        Item item = createItem(user);
        Review review = Review.builder().content("리뷰").item(item).createdBy("seller").build();

        given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(user));
        given(likeRepository.existsByReview_IdAndUser_Id(review.getId(), user.getId())).willReturn(true);

        //when
        likeService.reviewLike(user.getUserName(), review.getId());

        //then
        verify(likeRepository).deleteByReview_IdAndUser_Id(any(), any());
    }

    private User sellerUser() {
        return User.builder().userName("seller")
                .password("1234")
                .storeName("회사")
                .roleType(RoleType.SELLER)
                .build();
    }

    private Item createItem(User user) {
        return Item.builder()
                .user(user)
                .itemName("item").quantity(100).price(10000).build();
    }
}