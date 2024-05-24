package com.vicheak.mbankingapi.api.user.web;

import jakarta.validation.constraints.*;

import java.util.Set;

public record CreateUserDto(@NotBlank(message = "Username must not be blank!")
                            String username,
                            @NotBlank(message = "Email must not be blank!")
                            @Email(message = "Email must be in valid form!")
                            String email,
                            @NotBlank(message = "Password must not be blank!")
                            @Size(min = 8, message = "Password must be at least 8 characters!")
                            String password,
                            @NotBlank(message = "Gender must not be blank!")
                            String gender,
                            @NotBlank(message = "Phone number must not be blank!")
                            String phoneNumber,
                            String oneSignalId,
                            Boolean isStudent,
                            String studentCardNo,
                            @NotNull(message = "Roles must not be null")
                            @Size(min = 1, message = "User must have at least one role!")
                            Set<@NotNull(message = "Role must not be null")
                            @Positive(message = "Role must be positive!") Integer> roleIds) {
}
