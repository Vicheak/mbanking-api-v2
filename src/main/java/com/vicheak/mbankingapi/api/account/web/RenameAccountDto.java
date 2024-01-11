package com.vicheak.mbankingapi.api.account.web;

import jakarta.validation.constraints.NotBlank;

public record RenameAccountDto(@NotBlank(message = "Name must not be blank!")
                               String renameTo) {
}
