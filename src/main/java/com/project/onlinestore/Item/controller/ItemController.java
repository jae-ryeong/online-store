package com.project.onlinestore.Item.controller;

import com.project.onlinestore.Item.dto.request.DeleteItemRequestDto;
import com.project.onlinestore.Item.dto.request.RegistrationRequestDto;
import com.project.onlinestore.Item.dto.request.itemQuantityRequestDto;
import com.project.onlinestore.Item.dto.response.CategoryItemResponseDto;
import com.project.onlinestore.Item.dto.response.ItemSearchResponseDto;
import com.project.onlinestore.Item.dto.response.RegistrationResponseDto;
import com.project.onlinestore.Item.dto.response.itemQuantityResponseDto;
import com.project.onlinestore.Item.entity.enums.Category;
import com.project.onlinestore.Item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/registration")
    public ResponseEntity<RegistrationResponseDto> registration(Authentication authentication, @RequestBody RegistrationRequestDto dto) throws IOException {

        RegistrationResponseDto responseDto = itemService.createItem(authentication.getName(), dto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(Authentication authentication, @RequestBody DeleteItemRequestDto dto) {

        itemService.deleteItem(authentication.getName(), dto.itemId());

        return ResponseEntity.status(HttpStatus.OK)
                .body("등록하신 상품이 삭제되었습니다.");
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ItemSearchResponseDto>> searchAll(Pageable pageable) {
        Page<ItemSearchResponseDto> allItem = itemService.findAllItem(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(allItem);
    }

    @PutMapping("/{itemId}/quantityupdate")
    public ResponseEntity<itemQuantityResponseDto> quantityUpdate(Authentication authentication, @PathVariable("itemId") Long itemId, @RequestBody itemQuantityRequestDto dto) {
        itemQuantityResponseDto itemQuantityResponseDto = itemService.itemQuantityUpdate(authentication.getName(), itemId, dto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(itemQuantityResponseDto);
    }

    @PostMapping("/find/{category}")
    public ResponseEntity<List<CategoryItemResponseDto>> itemCategoryFind(@PathVariable("category") String category) {
        List<CategoryItemResponseDto> byCategory = itemService.findByCategory(category);

        return ResponseEntity.status(HttpStatus.OK)
                .body(byCategory);
    }
}
