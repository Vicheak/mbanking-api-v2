package com.vicheak.mbankingapi.base;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BaseApi<T>(Boolean isSuccess,
                         String message,
                         Integer code,
                         LocalDateTime timestamp,
                         T payload) {
}
