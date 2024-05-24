package com.vicheak.mbankingapi.api.account.web;

import com.vicheak.mbankingapi.api.account.AccountService;
import com.vicheak.mbankingapi.base.BaseApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
    @PutMapping("/{uuid}/rename")
    public void renameAccountByUuid(@PathVariable String uuid,
                                    @RequestBody @Valid RenameAccountDto renameAccountDto){
        accountService.renameAccountByUuid(uuid, renameAccountDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{uuid}/limit-transfer")
    public void updateTransferLimitByUuid(@PathVariable String uuid,
                                          @RequestBody @Valid TransferLimitAccountDto transferLimitAccountDto){
        accountService.updateTransferLimitByUuid(uuid, transferLimitAccountDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public BaseApi<?> loadAllAccounts(){
        return BaseApi.builder()
                .isSuccess(true)
                .message("Accounts loaded successfully!")
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .payload(accountService.loadAllAccounts())
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{uuid}")
    public BaseApi<?> loadAccountByUuid(@PathVariable String uuid){
        return BaseApi.builder()
                .isSuccess(true)
                .message("Account loaded successfully!")
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .payload(accountService.loadAccountByUuid(uuid))
                .build();
    }

}
