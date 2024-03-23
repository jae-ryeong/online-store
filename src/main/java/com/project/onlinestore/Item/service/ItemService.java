package com.project.onlinestore.Item.service;

import com.project.onlinestore.Item.dto.request.RegistrationRequestDto;
import com.project.onlinestore.Item.dto.response.ItemSearchResponseDto;
import com.project.onlinestore.Item.dto.response.RegistrationResponseDto;
import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.repository.ItemRepository;
import com.project.onlinestore.Item.repository.LikeRepository;
import com.project.onlinestore.Item.repository.ReviewRepository;
import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.exception.ErrorCode;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.entity.enums.RoleType;
import com.project.onlinestore.user.repository.ItemCartRepository;
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
    private final ItemCartRepository itemCartRepository;
    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;

    public RegistrationResponseDto registration(String userName, RegistrationRequestDto dto) {
        User user = findUser(userName);

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
                        .user(user)
                        .soldOut(false)
                        .count(0L)
                        .build()
        );

        return new RegistrationResponseDto(dto.itemName(), dto.quantity(), dto.price(), user.getId(), user.getStoreName(), dto.category());
    }

    @Transactional
    public void deleteItem(String userName, Long itemId) {
        User user = findUser(userName);
        Item item = findItem(itemId);

        if(item.getUser() != user){
            throw new ApplicationException(ErrorCode.INVALID_USER, "권한이 없는 유저 입니다.");
        }

        // Item 삭제시 장바구니속 해당 item, item에 달린 review, item에 눌린 좋아요를 모두 삭제한다.
        itemRepository.deleteById(itemId);
        itemCartRepository.deleteAllByItem(item);
        reviewRepository.deleteAllByItem(item);
        likeRepository.deleteAllByItem_Id(itemId);
    }

    @Transactional
    public void itemSoldOutCheck(Long itemId, Integer itemCount) {
        Item item = findItem(itemId);

        if (item.getQuantity() - itemCount <= 0){
            itemRepository.itemSoldOut(itemId);
        }
    }
    // TODO: 아이템 재고 +, - (판매자가)

    public Page<ItemSearchResponseDto> findAllItem(Pageable pageable) {
        return itemRepository.findAll(pageable).map(ItemSearchResponseDto::fromEntity);
    }

    private User findUser(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() ->
                new ApplicationException(ErrorCode.USERNAME_NOT_FOUND, userName + "를 찾을 수 없습니다."));
    }

    private Item findItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new ApplicationException(ErrorCode.ITEM_ID_NOT_FOUND, itemId + "를 찾을 수 없습니다."));
    }
}
