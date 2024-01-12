package com.vicheak.mbankingapi.api.account.web;

import java.math.BigDecimal;

public record AccountDto(String number,
                         String name,
                         BigDecimal transferLimit,
                         String accountType) {
}
