package com.project.onlinestore.service;

import com.project.onlinestore.dto.request.CustomerRequestDto;
import com.project.onlinestore.dto.request.SellerRequestDto;
import com.project.onlinestore.entity.User;
import com.project.onlinestore.entity.enums.RoleType;
import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.exception.ErrorCode;
import com.project.onlinestore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void CustomerJoin(CustomerRequestDto dto) {
        userRepository.findByUserName(dto.userName()).ifPresent(user -> {
            throw new ApplicationException(ErrorCode.DUPLICATED_USERNAME, String.format("%s는 이미 존재하는 아이디입니다.", dto.userName()));
        });

        userRepository.save(User.builder()
                .userName(dto.userName())
                .password(dto.password())
                .roleType(RoleType.CUSTOMER)
                .build()
        );
    }

    @Transactional
    public void SellerJoin(SellerRequestDto dto) {
        userRepository.findByUserName(dto.userName()).ifPresent(User -> {
            throw new ApplicationException(ErrorCode.DUPLICATED_USERNAME, String.format("%s는 이미 존재하는 아이디입니다.", dto.userName()));
        });

        userRepository.save(User.builder()
                .userName(dto.userName())
                .password(dto.password())
                .roleType(RoleType.SELLER)
                .storeName(dto.storeName())
                .build()
        );
    }

}
