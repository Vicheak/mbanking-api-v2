package com.vicheak.mbankingapi.api.auth.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordDto(@NotBlank(message = "Email must not be blank!")
                                @Email(message = "Email must be in valid form!")
                                String email,
                                @NotBlank(message = "Previous password must not be blank!")
                                @Size(min = 8, message = "Password must be at least 8 characters!")
                                String oldPassword,
                                @NotBlank(message = "New password must not be blank!")
                                @Size(min = 8, message = "Password must be at least 8 characters!")
                                String newPassword) {
}
