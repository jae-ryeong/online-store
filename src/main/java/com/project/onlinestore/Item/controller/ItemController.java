package com.project.onlinestore.Item.controller;

import com.project.onlinestore.Item.dto.request.RegistrationRequestDto;
import com.project.onlinestore.Item.dto.response.RegistrationResponseDto;
import com.project.onlinestore.Item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/registration")
    public ResponseEntity<RegistrationResponseDto> registration(@RequestBody RegistrationRequestDto dto, Authentication authentication) {

        RegistrationResponseDto registration = itemService.Registration(authentication.getName(), dto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(registration);
    }

}
