package com.project.onlinestore.exception;

import com.project.onlinestore.exception.dto.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorDto> handleApplicationException(ApplicationException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorDto errorDto = new ErrorDto(errorCode, e.getMessage());

        return ResponseEntity.status(errorCode.getStatus()).body(errorDto);
    }
}
