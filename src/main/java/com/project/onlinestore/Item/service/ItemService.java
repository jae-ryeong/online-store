package com.project.onlinestore.Item.service;

import com.project.onlinestore.Item.dto.request.RegistrationRequestDto;
import com.project.onlinestore.Item.dto.response.ItemSearchResponseDto;
import com.project.onlinestore.Item.dto.response.RegistrationResponseDto;
import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.repository.ItemRepository;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.entity.enums.RoleType;
import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.exception.ErrorCode;
import com.project.onlinestore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public RegistrationResponseDto registration(String userName, RegistrationRequestDto dto) {
        User user = userRepository.findByUserName(userName).orElseThrow(() ->
                new ApplicationException(ErrorCode.USERNAME_NOT_FOUND, userName + "를 찾을 수 없습니다."));
        
        // seller type check
        if (user.getRoleType() == RoleType.CUSTOMER) {
            throw new ApplicationException(ErrorCode.INVALID_ROLE, "접근권한이 없습니다.");
        }

        itemRepository.save(
                Item.builder()
                        .itemName(dto.itemName())
                        .quantity(dto.quantity())
                        .price(dto.price())
                        .category(dto.category())
                        .like(0)
                        .user(user)
                        .soldOut(false)
                        .count(0L)
                        .build()
        );

        return new RegistrationResponseDto(dto.itemName(), dto.quantity(), dto.price(), user.getId(), user.getStoreName(), dto.category());
    }

    public Page<ItemSearchResponseDto> findAllItem (Pageable pageable) {
        return itemRepository.findAll(pageable).map(ItemSearchResponseDto::fromEntity);
    }
}
