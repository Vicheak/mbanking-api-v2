package com.vicheak.mbankingapi.security.authorityconfig;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TransactionAuth {
    TRANSACTION_READ("transaction:read"),
    TRANSACTION_WRITE("transaction:write"),
    TRANSACTION_UPDATE("transaction:update"),
    TRANSACTION_DELETE("transaction:delete");

    private final String name;
}
