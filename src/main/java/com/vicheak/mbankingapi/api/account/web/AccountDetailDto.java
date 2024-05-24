package com.vicheak.mbankingapi.api.account.web;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class AccountDetailDto {

    private String number;
    private String name;
    private BigDecimal transferLimit;
    private String accountType;
    private Boolean isDisabled;

}
