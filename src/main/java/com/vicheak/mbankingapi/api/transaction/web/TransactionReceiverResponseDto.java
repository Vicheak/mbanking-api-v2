package com.vicheak.mbankingapi.api.transaction.web;

import lombok.Builder;

@Builder
public record TransactionReceiverResponseDto(String receiverAccountNumber,
                                             String receiverAccountName,
                                             String receiverAccountType) {
}
