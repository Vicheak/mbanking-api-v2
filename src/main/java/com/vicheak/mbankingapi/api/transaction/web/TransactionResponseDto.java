package com.vicheak.mbankingapi.api.transaction.web;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TransactionResponseDto(String uuid,
                                     TransactionSenderResponseDto transactionSender,
                                     TransactionReceiverResponseDto transactionReceiver,
                                     BigDecimal amount,
                                     String remark,
                                     LocalDateTime transactionAt,
                                     Boolean isPayment) {
}
