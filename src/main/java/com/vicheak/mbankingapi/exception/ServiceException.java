package com.vicheak.mbankingapi.exception;

import com.vicheak.mbankingapi.base.BaseError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ServiceException {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleServiceException(ResponseStatusException ex){
        return new ResponseEntity<>(BaseError.builder()
                .status(false)
                .message("Something went wrong!")
                .code(ex.getStatusCode().value())
                .timestamp(LocalDateTime.now())
                .error(ex.getReason())
                .build(), ex.getStatusCode());
    }

}
