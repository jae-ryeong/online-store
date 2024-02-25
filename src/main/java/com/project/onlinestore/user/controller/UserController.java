package com.project.onlinestore.user.controller;

import com.project.onlinestore.user.dto.request.*;
import com.project.onlinestore.user.dto.response.*;
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

    @PutMapping("/cart/view/check")
    public ResponseEntity<CartCheckResponseDto> cartCheck(Authentication authentication, @RequestBody CartCheckRequestDto dto) {
        // 체크 버튼 클릭시 toggle
        CartCheckResponseDto cartCheckResponseDto = cartService.cartCheck(authentication.getName(), dto.itemCartId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(cartCheckResponseDto);
    }

    @DeleteMapping("/cart/view/delete/{itemCartId}")
    public ResponseEntity<String> cartDelete(Authentication authentication, @PathVariable("itemCartId") Long itemCartId) {
        cartService.delete(authentication.getName(), itemCartId);

        return ResponseEntity.status(HttpStatus.OK)
                .body("장바구니 목록이 삭제되었습니다.");
    }
}
