package com.vicheak.mbankingapi.api.user.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateUserDto(@NotBlank(message = "Username must not be blank!")
                            String username,
                            @NotBlank(message = "Gender must not be blank!")
                            String gender,
                            @NotNull(message = "Roles must not be null")
                            @Size(min = 1, message = "User must have at least one role!")
                            Set<@NotNull(message = "Role must not be null")
                            @Positive(message = "Role must be positive!") Integer> roleIds) {
}
