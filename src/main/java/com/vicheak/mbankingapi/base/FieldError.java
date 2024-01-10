package com.vicheak.mbankingapi.base;

import lombok.Builder;

@Builder
public record FieldError(String field,
                         String message) {
}
