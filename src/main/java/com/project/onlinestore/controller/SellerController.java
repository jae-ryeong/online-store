package com.project.onlinestore.controller;

import com.project.onlinestore.dto.UserDto;
import com.project.onlinestore.dto.request.SellerRequestDto;
import com.project.onlinestore.dto.response.UserResponseDto;
import com.project.onlinestore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
public class SellerController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserResponseDto> join(@RequestBody SellerRequestDto dto) {
        userService.SellerJoin(dto);
        UserResponseDto userResponseDto = UserResponseDto.fromSellerDto(dto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userResponseDto);
    }
}
