package com.vicheak.mbankingapi.api.auth.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendVerifyDto(@NotBlank(message = "Email must not be blank!")
                            @Email(message = "Email must be in valid form!")
                            String email) {
}
