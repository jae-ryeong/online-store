package com.project.onlinestore.Item.controller;

import com.project.onlinestore.Item.dto.request.LikeClickRequestDto;
import com.project.onlinestore.Item.dto.response.ItemLikeResponseDto;
import com.project.onlinestore.Item.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/click")
    public ResponseEntity<ItemLikeResponseDto> click(@RequestBody LikeClickRequestDto dto, Authentication authentication) {
        ItemLikeResponseDto itemLikeResponseDto = likeService.itemLike(dto.itemId(), authentication.getName());

        return ResponseEntity.status(OK)
                .body(itemLikeResponseDto);
    }
}
