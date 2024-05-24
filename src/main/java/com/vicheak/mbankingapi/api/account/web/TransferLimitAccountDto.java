package com.vicheak.mbankingapi.api.account.web;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferLimitAccountDto(@NotNull(message = "Transfer limit must not be null!")
                                      @DecimalMin(value = "1000", message = "Transfer limit must be at least 1000!")
                                      BigDecimal transferLimit) {
}
