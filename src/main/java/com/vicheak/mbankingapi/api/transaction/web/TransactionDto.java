package com.vicheak.mbankingapi.api.transaction.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransactionDto(@NotBlank(message = "Receiver account number must not be blank!")
                             String receiverAccountNumber,
                             @NotBlank(message = "Sender account number must not be blank!")
                             String senderAccountNumber,
                             @NotNull(message = "Amount must not be null!")
                             @Positive(message = "Amount must be positive!")
                             BigDecimal amount,
                             String remark,
                             @NotNull(message = "isPayment field is required! Is the transaction payment or just transfer?")
                             Boolean isPayment) {
}
