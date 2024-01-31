package com.project.onlinestore.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApplicationException extends RuntimeException {

    private ErrorCode errorCode;
    private String errorMessage;

    @Override
    public String getMessage() {
        if (errorMessage == null) {
            return errorCode.getErrorMessage();
        }
        return String.format("%s, %s", errorCode.getErrorMessage(), errorMessage);
    }
}
