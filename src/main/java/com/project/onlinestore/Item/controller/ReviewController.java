package com.project.onlinestore.Item.controller;

import com.project.onlinestore.Item.dto.request.ReviewCreateRequestDto;
import com.project.onlinestore.Item.dto.response.ReviewCreateResponseDto;
import com.project.onlinestore.Item.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
