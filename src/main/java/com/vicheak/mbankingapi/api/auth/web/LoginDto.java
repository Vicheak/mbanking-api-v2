package com.vicheak.mbankingapi.api.auth.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDto(@NotBlank(message = "Username must not be blank!")
                       String username,
                       @NotBlank(message = "Password must not be blank!")
                       @Size(min = 8, message = "Password must be at least 8 characters!")
                       String password) {
}
