package com.project.onlinestore.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USERNAME(HttpStatus.CONFLICT, "userName is duplicated"),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "userName not found");

    private HttpStatus status;
    private String errorMessage;
}
