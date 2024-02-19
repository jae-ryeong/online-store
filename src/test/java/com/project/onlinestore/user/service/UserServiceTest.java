package com.project.onlinestore.user.service;

import com.project.onlinestore.user.dto.request.CustomerRequestDto;
import com.project.onlinestore.user.repository.CartRepository;
import com.project.onlinestore.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private BCryptPasswordEncoder encoder;

    @DisplayName("customer 회원 가입")
    @Test
    public void customerJoinTest() throws Exception{
        //given
        CustomerRequestDto customer = new CustomerRequestDto("customer", "1234");

        //when
        userService.customerJoin(customer);

        //then
        verify(cartRepository).save(any());
        verify(userRepository).save(any());
    }
}