package com.vicheak.mbankingapi.api.account.web;

import com.vicheak.mbankingapi.api.account.AccountTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/account-types")
@RequiredArgsConstructor
public class AccountTypeController {

    private final AccountTypeService accountTypeService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<?> loadAllAccountTypes() {
        return accountTypeService.loadAllAccountTypes();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public AccountTypeDto loadAccountTypeById(@PathVariable Integer id) {
        return accountTypeService.loadAccountTypeById(id);
    }

}
