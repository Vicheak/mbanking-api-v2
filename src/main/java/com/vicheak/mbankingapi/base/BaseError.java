package com.vicheak.mbankingapi.base;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BaseError<T>(Boolean status,
                           String message,
                           Integer code,
                           LocalDateTime timestamp,
                           T error) {
}
