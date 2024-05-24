package com.vicheak.mbankingapi.api.transaction.web;

import lombok.Builder;

@Builder
public record TransactionSenderResponseDto(String senderAccountNumber,
                                           String senderAccountName,
                                           String senderAccountType) {
}
