package com.project.onlinestore.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USERNAME(HttpStatus.CONFLICT, "userName is duplicated"),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "userName not found"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is invalid");

    private HttpStatus status;
    private String errorMessage;
}
