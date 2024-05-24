package com.vicheak.mbankingapi.security.authorityconfig;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AccountAuth {
    ACCOUNT_RAED("account:read"),
    ACCOUNT_WRITE("account:write"),
    ACCOUNT_UPDATE("account:update"),
    ACCOUNT_DELETE("account:delete");

    private final String name;
}
