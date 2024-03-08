package com.project.onlinestore.user.service;

import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.user.dto.request.AddressRegistrationRequestDto;
import com.project.onlinestore.user.dto.response.AddressRegistrationResponseDto;
import com.project.onlinestore.user.entity.Address;
import com.project.onlinestore.user.entity.Cart;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.entity.enums.RoleType;
import com.project.onlinestore.user.repository.AddressRepository;
import com.project.onlinestore.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;

    @Mock
    private AddressRepository addressRepository;
    @Mock
    private UserRepository userRepository;

    @DisplayName("주소지 등록 테스트")
    @Test
    void addressRegistrationTest() {
        //given
        Cart cart = createCart();
        User user = customerUser(cart);
        AddressRegistrationRequestDto responseDto = new AddressRegistrationRequestDto("주문자","대전시", "동구", 12345, "010-0000-1234");

        given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(user));

        //when
        AddressRegistrationResponseDto resultDto = addressService.addressRegistration(user.getUserName(), responseDto);

        //then
        verify(addressRepository).save(any());
        assertThat(resultDto.tel()).isEqualTo("010-0000-1234");
    }

    @DisplayName("주소지 등록시 전화번호가 패턴에 맞지 않으면 에러 발생")
    @Test
    void addressRegistrationPatternErrorTest() {
        //given
        Cart cart = createCart();
        User user = customerUser(cart);
        AddressRegistrationRequestDto responseDto = new AddressRegistrationRequestDto("주문자","대전시", "동구", 12345, "1235");

        given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(user));

        //then
        assertThatThrownBy(() -> addressService.addressRegistration(user.getUserName(), responseDto))
                .isInstanceOf(ApplicationException.class);
    }

    @DisplayName("주소지 삭제 테스트")
    @Test
    void addressDeleteTest() {
        //given
        Cart cart = createCart();
        User user = customerUser(cart);
        Address address = Address.builder().detailAddress("213").address("321").user(user).tel("010-0000-0000").build();

        given(userRepository.findByUserName(any())).willReturn(Optional.of(user));
        given(addressRepository.findById(any())).willReturn(Optional.of(address));

        //when
        addressService.addressDelete("customer", any());

        //then
        verify(addressRepository).deleteById(any());
    }

    @DisplayName("다른 유저가 주소지 삭제시 에러 발생")
    @Test
    void addressDeleteErrorTest() {
        //given
        Cart cart = createCart();
        User user = customerUser(cart);
        User user2 = User.builder().build();
        Address address = Address.builder().detailAddress("213").address("321").user(user).tel("010-0000-0000").build();

        given(userRepository.findByUserName(any())).willReturn(Optional.of(user2));
        given(addressRepository.findById(any())).willReturn(Optional.of(address));

        //then
        assertThatThrownBy(() -> addressService.addressDelete("customer", any()))
                .isInstanceOf(ApplicationException.class);
    }

    private User customerUser(Cart cart) {
        return User.builder().userName("customer")
                .password("1234")
                .cart(cart)
                .roleType(RoleType.CUSTOMER)
                .build();
    }

    private Cart createCart() {
        return Cart.builder()
                .build();
    }
}