package com.project.onlinestore.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USERNAME(HttpStatus.CONFLICT, "userName is duplicated"),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "userName not found"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is invalid"),
    INVALID_ROLE(HttpStatus.FORBIDDEN, "Not Authorized RoleType"),
    ITEM_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "Item not fount"),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "Cart not found"),
    ITEM_CART_NOT_FOUNT(HttpStatus.NOT_FOUND, "ItemCart not found"),
    INVALID_USER(HttpStatus.FORBIDDEN, "Not Authorized USER");

    private HttpStatus status;
    private String errorMessage;
}
