package com.project.onlinestore.Item.controller;

import com.project.onlinestore.Item.dto.response.ItemLikeResponseDto;
import com.project.onlinestore.Item.dto.response.ReviewLikeResponseDto;
import com.project.onlinestore.Item.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/item/{itemId}")
    public ResponseEntity<ItemLikeResponseDto> itemLike(@PathVariable("itemId")Long itemId, Authentication authentication) {
        ItemLikeResponseDto itemLikeResponseDto = likeService.itemLike(itemId, authentication.getName());

        return ResponseEntity.status(OK)
                .body(itemLikeResponseDto);
    }

    @PostMapping("/review/{reviewId}")
    public ResponseEntity<ReviewLikeResponseDto> reviewLike(@PathVariable("reviewId")Long reviewId, Authentication authentication) {
        ReviewLikeResponseDto reviewLikeResponseDto = likeService.reviewLike(authentication.getName(), reviewId);

        return ResponseEntity.status(OK)
                .body(reviewLikeResponseDto);
    }
}
