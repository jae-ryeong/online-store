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
import com.project.onlinestore.order.Entity.Order;
import com.project.onlinestore.order.repository.OrderItemRepository;
import com.project.onlinestore.order.repository.OrderRepository;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final OrderItemRepository orderItemRepository;

    public ReviewCreateResponseDto createReview(String userName, ReviewCreateRequestDto dto) {
        User user = findUser(userName);
        Item item = findItem(dto.itemId());

        if (reviewRepository.existsByItemAndCreatedBy(item, userName)){
            throw new ApplicationException(ErrorCode.DUPLICATED_REVIEW, "이미 리뷰를 작성한 상품입니다.");
        }

        List<Order> orderList = orderRepository.findAllByUser_Id(user.getId());

        for (Order order : orderList) {
            if (orderItemRepository.existsByOrderAndItem(order.getId(), item.getId())){ // 주문한 상품만 리뷰 등록 가능
                if (order.getOrderDate().isBefore(LocalDateTime.now().minusDays(30))){  // 주문 하고 30일 이내에만 리뷰 작성 가능
                    continue;
                }
                reviewRepository.save(
                        Review.builder()
                                .item(item)
                                .content(dto.content())
                                .build()
                );
                return new ReviewCreateResponseDto(dto.itemId(), user.getId(), dto.content());
            }
        }
        throw new ApplicationException(ErrorCode.CAN_NOT_WRITE_REVIEW, null);
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
