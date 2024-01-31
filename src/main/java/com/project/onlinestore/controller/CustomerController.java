package com.project.onlinestore.controller;

import com.project.onlinestore.dto.request.CustomerRequestDto;
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
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserResponseDto> join(@RequestBody CustomerRequestDto dto) {
        userService.CustomerJoin(dto);
        UserResponseDto userResponseDto = UserResponseDto.fromCustomerDto(dto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userResponseDto);
    }
}
