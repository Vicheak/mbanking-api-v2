package com.vicheak.mbankingapi.security.authorityconfig;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RoleAuth {
    ADMIN("ADMIN"),
    MANAGER("MANAGER"),
    CUSTOMER("CUSTOMER");

    private final String name;

    public SimpleGrantedAuthority getRoleName(){
        return new SimpleGrantedAuthority(name);
    }
}
