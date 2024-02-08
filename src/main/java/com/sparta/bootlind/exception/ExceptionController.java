package com.sparta.bootlind.exception;

import com.sparta.bootlind.dto.responseDto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(final IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(400, ex.getMessage()));
    }
}
