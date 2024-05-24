package com.vicheak.mbankingapi.api.transaction.web;

import com.vicheak.mbankingapi.api.transaction.TransactionService;
import com.vicheak.mbankingapi.base.BaseApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BaseApi<?> doTransaction(@RequestBody @Valid TransactionDto transactionDto) {
        transactionService.doTransaction(transactionDto);
        return BaseApi.builder()
                .isSuccess(true)
                .message("Transaction completed!")
                .code(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .payload("Transaction no payload!")
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public BaseApi<?> loadAllTransactions() {
        return BaseApi.builder()
                .isSuccess(true)
                .message("Transactions loaded!")
                .code(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .payload(transactionService.loadAllTransactions())
                .build();
    }

}
