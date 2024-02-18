package com.project.onlinestore.user.controller;

import com.project.onlinestore.user.dto.request.AddCartRequestDto;
import com.project.onlinestore.user.dto.request.CustomerRequestDto;
import com.project.onlinestore.user.dto.request.LoginRequestDto;
import com.project.onlinestore.user.dto.request.SellerRequestDto;
import com.project.onlinestore.user.dto.response.AddCartResponseDto;
import com.project.onlinestore.user.dto.response.CartViewResponseDto;
import com.project.onlinestore.user.dto.response.LoginResponseDto;
import com.project.onlinestore.user.dto.response.UserResponseDto;
import com.project.onlinestore.user.service.CartService;
import com.project.onlinestore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CartService cartService;

    @PostMapping("/customer/join")
    public ResponseEntity<UserResponseDto> join(@RequestBody CustomerRequestDto dto) {
        userService.customerJoin(dto);
        UserResponseDto userResponseDto = UserResponseDto.fromCustomerDto(dto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userResponseDto);
    }

    @PostMapping("/seller/join")
    public ResponseEntity<UserResponseDto> join(@RequestBody SellerRequestDto dto) {
        userService.sellerJoin(dto);
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

    @PostMapping("/cart/add")
    public ResponseEntity<AddCartResponseDto> addCart(@RequestBody AddCartRequestDto dto, Authentication authentication) {
        AddCartResponseDto addCartResponseDto = cartService.addCart(authentication.getName(), dto.itemId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(addCartResponseDto);
    }

    @GetMapping("/cart/view")
    public ResponseEntity<List<CartViewResponseDto>> allSearch(Authentication authentication) {
        List<CartViewResponseDto> itemCarts = cartService.allItemCartView(authentication.getName());

        return ResponseEntity.status(HttpStatus.OK)
                .body(itemCarts);
    }
}
