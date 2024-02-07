package com.project.onlinestore.Item.service;

import com.project.onlinestore.Item.dto.request.RegistrationRequestDto;
import com.project.onlinestore.Item.dto.response.RegistrationResponseDto;
import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.repository.ItemRepository;
import com.project.onlinestore.entity.User;
import com.project.onlinestore.entity.enums.RoleType;
import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.exception.ErrorCode;
import com.project.onlinestore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public RegistrationResponseDto Registration(String userName, RegistrationRequestDto dto) {
        User user = userRepository.findByUserName(userName).orElseThrow(() ->
                new ApplicationException(ErrorCode.USERNAME_NOT_FOUND, userName + "를 찾을 수 없습니다."));

        // seller type check
        if (user.getRoleType() != RoleType.ADMIN) {
            new ApplicationException(ErrorCode.INVALID_ROLE, "접근권한이 없습니다.");
        }

        itemRepository.save(
                Item.builder()
                        .itemName(dto.itemName())
                        .quantity(dto.quantity())
                        .price(dto.price())
                        .like(0)
                        .build()
        );

        return new RegistrationResponseDto(dto.itemName(), dto.quantity(), dto.price(), user.getId(), user.getStoreName());
    }
}
