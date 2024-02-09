package com.project.onlinestore.user.controller;

import com.project.onlinestore.user.dto.request.CustomerRequestDto;
import com.project.onlinestore.user.dto.request.LoginRequestDto;
import com.project.onlinestore.user.dto.request.SellerRequestDto;
import com.project.onlinestore.user.dto.response.LoginResponseDto;
import com.project.onlinestore.user.dto.response.UserResponseDto;
import com.project.onlinestore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/customer/join")
    public ResponseEntity<UserResponseDto> join(@RequestBody CustomerRequestDto dto) {
        userService.CustomerJoin(dto);
        UserResponseDto userResponseDto = UserResponseDto.fromCustomerDto(dto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userResponseDto);
    }

    @PostMapping("/seller/join")
    public ResponseEntity<UserResponseDto> join(@RequestBody SellerRequestDto dto) {
        userService.SellerJoin(dto);
        UserResponseDto userResponseDto = UserResponseDto.fromSellerDto(dto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        LoginResponseDto responseDto = userService.login(dto.userName(), dto.password());

        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDto);
    }
}
