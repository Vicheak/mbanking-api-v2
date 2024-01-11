package com.vicheak.mbankingapi.api.account.web;

import jakarta.validation.constraints.*;

public record CreateAccountDto(@NotBlank(message = "Account name must not be blank!")
                               String name,
                               @NotBlank(message = "Account pin must not be blank!")
                               @Size(min = 6, max = 6, message = "Account pin must be a six-digit pin!")
                               String pin,
                               @NotNull(message = "Account type must not be null!")
                               @Positive(message = "Account type must be positive!")
                               Integer accountTypeId) {
}
