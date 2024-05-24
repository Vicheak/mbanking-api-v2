package com.vicheak.mbankingapi.exception;

import com.vicheak.mbankingapi.base.BaseError;
import com.vicheak.mbankingapi.base.FieldError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ValidationException {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseError<?> handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldError> errors = new ArrayList<>();

        ex.getFieldErrors().forEach(fieldError ->
                errors.add(FieldError.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build()));

        return BaseError.builder()
                .status(false)
                .message("Something went wrong!")
                .code(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .error(errors)
                .build();
    }

}
