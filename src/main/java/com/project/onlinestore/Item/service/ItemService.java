package com.project.onlinestore.Item.service;

import com.project.onlinestore.Item.dto.request.RegistrationRequestDto;
import com.project.onlinestore.Item.dto.request.itemQuantityRequestDto;
import com.project.onlinestore.Item.dto.response.CategoryItemResponseDto;
import com.project.onlinestore.Item.dto.response.ItemSearchResponseDto;
import com.project.onlinestore.Item.dto.response.RegistrationResponseDto;
import com.project.onlinestore.Item.dto.response.itemQuantityResponseDto;
import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.entity.enums.Category;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemCartRepository itemCartRepository;
    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;

    @Transactional(rollbackFor = IOException.class) // 이미지 업로드 실패 시 DB 롤백
    public RegistrationResponseDto createItem(String userName, RegistrationRequestDto dto) throws IOException{
        User user = findUser(userName);
        // seller type check
        if (user.getRoleType() == RoleType.CUSTOMER) {
            throw new ApplicationException(ErrorCode.INVALID_ROLE, "접근권한이 없습니다.");
        }

        // Item 엔티티 생성
        Item item = Item.builder()
                .itemName(dto.itemName())
                .quantity(dto.quantity())
                .price(dto.price())
                .soldCount(0L)
                .category(dto.category())
                .soldOut(false)
                .mainImageUrl(dto.mainImageUrl())
                .description(dto.description())
                .user(user)
                .build();

        Item savedItem = itemRepository.save(item);
        return new RegistrationResponseDto(savedItem.getItemName(),
                savedItem.getQuantity(),
                savedItem.getPrice(),
                savedItem.getUser().getId(), savedItem.getUser().getStoreName(), savedItem.getCategory(), savedItem.getMainImageUrl(), savedItem.getDescription());
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
            itemRepository.itemSoldOutTrue(itemId);
        }
    }

    @Transactional
    public itemQuantityResponseDto itemQuantityUpdate(String userName, Long itemId, itemQuantityRequestDto dto) {
        User seller = findUser(userName);
        Item item = findItem(itemId);

        if (seller.getRoleType() != RoleType.SELLER || item.getUser() != seller){
            throw new ApplicationException(ErrorCode.INVALID_USER, null);
        }

        int resultQuantity = item.getQuantity() + dto.quantity();

        if (resultQuantity < 0) {
            throw new ApplicationException(ErrorCode.QUANTITY_OUT_OF_RANGE, "재고는 0보다 작을 수 없습니다.");
        }

        // 재고 갯수를 파악해 soldOut 체크
        if (item.isSoldOut() == true && resultQuantity > 0){
            itemRepository.itemSoldOutFalse(itemId);
        }
        if (item.isSoldOut() == false && resultQuantity == 0){
            itemRepository.itemSoldOutTrue(itemId);
        }

        itemRepository.itemQuantityUpdate(dto.quantity(), itemId);

        return new itemQuantityResponseDto(seller.getStoreName(), itemId, dto.quantity(), resultQuantity);
    }

    @Transactional(readOnly = true)
    public List<CategoryItemResponseDto> findByCategory(String category) {

        String categoryUpper = category.toUpperCase();

        boolean match = Arrays.stream(Category.values())
                .anyMatch(type -> categoryUpper.contains(type.name()));

        if(!match){
            throw new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND, "존재하지 않는 CATEGORY 입니다.");
        }

        List<Item> allByCategory = itemRepository.findAllByCategory(Category.valueOf(categoryUpper));
        List<CategoryItemResponseDto> allItem = new ArrayList<>();

        for (Item item : allByCategory) {
            allItem.add(
                    CategoryItemResponseDto.builder()
                            .itemName(item.getItemName())
                            .price(item.getPrice())
                            .mainImageUrl(item.getMainImageUrl())
                            .build()
            );
        };
        return allItem;
    }

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
