package com.vicheak.mbankingapi.api.auth.web;

import jakarta.validation.constraints.*;

public record RegisterDto(@NotBlank(message = "Username must not be blank!")
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
                          String studentCardNo) {
}
