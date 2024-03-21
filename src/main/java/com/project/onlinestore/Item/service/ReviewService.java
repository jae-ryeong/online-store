package com.project.onlinestore.Item.service;

import com.project.onlinestore.Item.dto.request.ReviewCreateRequestDto;
import com.project.onlinestore.Item.dto.request.ReviewUpdateRequestDto;
import com.project.onlinestore.Item.dto.response.ReviewCreateResponseDto;
import com.project.onlinestore.Item.dto.response.ReviewUpdateResponseDto;
import com.project.onlinestore.Item.dto.response.ReviewViewResponseDto;
import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.entity.Review;
import com.project.onlinestore.Item.repository.ItemRepository;
import com.project.onlinestore.Item.repository.LikeRepository;
import com.project.onlinestore.Item.repository.ReviewRepository;
import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.exception.ErrorCode;
import com.project.onlinestore.order.repository.OrderRepository;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;
    private final OrderRepository orderRepository;

    public ReviewCreateResponseDto createReview(String userName, ReviewCreateRequestDto dto) {
        User user = findUser(userName);
        Item item = findItem(dto.itemId());

        if (reviewRepository.existsByItemAndCreatedBy(item, userName)){
            throw new ApplicationException(ErrorCode.DUPLICATED_REVIEW, "이미 리뷰를 작성한 상품입니다.");
        }

        // TODO: 차후 리뷰 등록은 구매한 유저만 가능하게 구현, 주문하고 30일 이내에만 가능

        reviewRepository.save(
                Review.builder()
                        .item(item)
                        .content(dto.content())
                        .build()
        );

        return new ReviewCreateResponseDto(dto.itemId(), user.getId(), dto.content());
    }

    @Transactional
    public ReviewUpdateResponseDto updateReview(String userName, Long reviewId, ReviewUpdateRequestDto dto) {
        User user = findUser(userName);
        Review review = findReview(reviewId);

        if (!Objects.equals(review.getCreatedBy(), user.getUserName())){
            throw new ApplicationException(ErrorCode.INVALID_USER, "잘못된 유저 접근입니다.");
        }

        reviewRepository.updateReviewContent(dto.content(), review.getId());

        return new ReviewUpdateResponseDto(review.getItem().getId(), user.getId(), review.getId(), dto.content());
    }

    @Transactional
    public void deleteReview(String userName, Long reviewId){
        User user = findUser(userName);
        Review review = findReview(reviewId);

        if (!Objects.equals(review.getCreatedBy(), user.getUserName())){
            throw new ApplicationException(ErrorCode.INVALID_USER, "잘못된 유저 접근입니다.");
        }

        reviewRepository.deleteById(reviewId);
        likeRepository.deleteAllByReview_Id(reviewId);
    }

    public List<ReviewViewResponseDto> ViewReview(Long itemId) {
        List<Review> allByItemId = reviewRepository.findAllByItem_Id(itemId);
        List<ReviewViewResponseDto> result = allByItemId.stream().map(r -> new ReviewViewResponseDto(r.getCreatedBy(), r.getCreatedAt(), r.getContent(), likeRepository.countByReview_Id(r.getId()))).toList();

        return result;
    }

    private User findUser(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() ->
                new ApplicationException(ErrorCode.USERNAME_NOT_FOUND, userName + "를 찾을 수 없습니다."));
    }

    private Item findItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new ApplicationException(ErrorCode.ITEM_ID_NOT_FOUND, "아이템을 찾을 수 없습니다."));
    }

    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() ->
                new ApplicationException(ErrorCode.REVIEW_NOT_FOUNT, "리뷰를 찾을 수 없습니다."));
    }
}
