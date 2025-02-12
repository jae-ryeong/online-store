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
    ITEM_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "Item not found"),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "Cart not found"),
    ITEM_CART_NOT_FOUNT(HttpStatus.NOT_FOUND, "ItemCart not found"),
    INVALID_USER(HttpStatus.FORBIDDEN, "Not Authorized USER"),
    REVIEW_NOT_FOUNT(HttpStatus.NOT_FOUND, "Review not found"),
    DUPLICATED_REVIEW(HttpStatus.CONFLICT, "Review is duplicated"),
    NOT_ENOUGH_QUANTITY(HttpStatus.CONFLICT,"Quantity is not enough"),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Order not found"),
    QUANTITY_OUT_OF_RANGE(HttpStatus.CONFLICT, "Cannot be less than 0"),
    DIFFERENT_FROM_THE_PATTERN(HttpStatus.CONFLICT, "It's different from the pattern"),
    ADDRESS_NOT_FOUNT(HttpStatus.NOT_FOUND, "Address not found"),
    CAN_NOT_CANCELED(HttpStatus.FORBIDDEN, "Orders that have been shipped cannot be canceled."),
    CAN_NOT_TAKE_BACK(HttpStatus.FORBIDDEN, "This order cannot be take back"),
    CAN_NOT_WRITE_REVIEW(HttpStatus.CONFLICT, "Can't write a review"),
    ORDER_ITEM_NOT_FOUNT_IN_ORDER(HttpStatus.NOT_FOUND, "I couldn't find the item in your order list"),
    NOT_TAKE_BACK_APPLICATION(HttpStatus.CONFLICT, "This product has not been requested to be returned."),
    NOT_ORDER_STATUS(HttpStatus.CONFLICT, "Order status is not Order"),
    NOT_FOUNT_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "Refresh token not found"),
    ACCESS_TOKEN_IN_BLACKLIST(HttpStatus.CONFLICT, "Access Token in blacklist"),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Category not found");

    private HttpStatus status;
    private String errorMessage;
}
