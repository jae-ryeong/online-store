package com.project.onlinestore.Item.controller;

import com.project.onlinestore.Item.dto.request.ReviewCreateRequestDto;
import com.project.onlinestore.Item.dto.request.ReviewUpdateRequestDto;
import com.project.onlinestore.Item.dto.response.ReviewCreateResponseDto;
import com.project.onlinestore.Item.dto.response.ReviewUpdateResponseDto;
import com.project.onlinestore.Item.dto.response.ReviewViewResponseDto;
import com.project.onlinestore.Item.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/create")
    public ResponseEntity<ReviewCreateResponseDto> reviewCreate(Authentication authentication, @RequestBody ReviewCreateRequestDto dto) {
        ReviewCreateResponseDto responseDto = reviewService.createReview(authentication.getName(), dto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDto);

    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<String> reviewDelete(Authentication authentication, @PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(authentication.getName(), reviewId);

        return ResponseEntity.status(HttpStatus.OK)
                .body("리뷰가 정상적으로 삭제되었습니다.");
    }

    @PutMapping("/update/{reviewId}")
    public ResponseEntity<ReviewUpdateResponseDto> reviewUpdate(Authentication authentication, @PathVariable("reviewId")Long reviewId, @RequestBody ReviewUpdateRequestDto dto){
        ReviewUpdateResponseDto reviewUpdateResponseDto = reviewService.updateReview(authentication.getName(), reviewId, dto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(reviewUpdateResponseDto);
    }

    @GetMapping("/view/{itemId}")
    public ResponseEntity<List<ReviewViewResponseDto>> reviewView(@PathVariable("itemId") Long itemId) {
        List<ReviewViewResponseDto> reviewViewResponseDto = reviewService.ViewReview(itemId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(reviewViewResponseDto);
    }
}
