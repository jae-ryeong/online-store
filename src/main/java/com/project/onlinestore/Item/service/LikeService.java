package com.project.onlinestore.Item.service;

import com.project.onlinestore.Item.dto.response.ItemLikeResponseDto;
import com.project.onlinestore.Item.dto.response.ReviewLikeResponseDto;
import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.entity.Like;
import com.project.onlinestore.Item.entity.Review;
import com.project.onlinestore.Item.repository.ItemRepository;
import com.project.onlinestore.Item.repository.LikeRepository;
import com.project.onlinestore.Item.repository.ReviewRepository;
import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.exception.ErrorCode;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final LikeRepository likeRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public ItemLikeResponseDto itemLike(Long itemId, String userName) {
        User user = findUser(userName);

        if (likeRepository.existsByItem_IdAndUser_Id(itemId, user.getId())) {   // 좋아요를 누른적이 있으면
            likeRepository.deleteByItem_IdAndUser_Id(itemId, user.getId());     // 좋아요 취소
        } else {
            Item item = findItem(itemId);
            likeRepository.save(
                    Like.builder()
                            .item(item)
                            .user(user)
                            .build()
            );
        }
        return new ItemLikeResponseDto(itemId, user.getId(), itemLikeCount(itemId));
    }

    @Transactional
    public ReviewLikeResponseDto reviewLike(String userName, Long reviewId) {
        User user = findUser(userName);

        if(likeRepository.existsByReview_IdAndUser_Id(reviewId, user.getId())){
            likeRepository.deleteByReview_IdAndUser_Id(reviewId, user.getId());
        } else{
            Review review = findReview(reviewId);
            likeRepository.save(
                    Like.builder()
                            .user(user)
                            .review(review)
                            .build()
            );
        }
        return new ReviewLikeResponseDto(user.getId(), reviewId, reviewLikeCount(reviewId));
    }

    private Integer itemLikeCount(Long itemId) {    // 좋아요 총 갯수
        return likeRepository.countByItem_Id(itemId);
    }
    private Integer reviewLikeCount(Long reviewId) {
        return likeRepository.countByReview_Id(reviewId);
    }

    private User findUser(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() ->
                new ApplicationException(ErrorCode.USERNAME_NOT_FOUND, userName + "를 찾을 수 없습니다."));
    }

    private Item findItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new ApplicationException(ErrorCode.ITEM_ID_NOT_FOUND, itemId + "를 찾을 수 없습니다."));
    }

    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() ->
                new ApplicationException(ErrorCode.REVIEW_NOT_FOUNT, "리뷰를 찾을 수 없습니다."));
    }
}
