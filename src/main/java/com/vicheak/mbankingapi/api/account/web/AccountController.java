package com.vicheak.mbankingapi.api.account.web;

import com.vicheak.mbankingapi.api.account.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createNewAccount(@RequestBody @Valid CreateAccountDto createAccountDto){
        accountService.createNewAccount(createAccountDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{uuid}")
    public void renameAccountByUuid(@PathVariable String uuid,
                                    @RequestBody @Valid RenameAccountDto renameAccountDto){
        accountService.renameAccountByUuid(uuid, renameAccountDto);
    }

}
