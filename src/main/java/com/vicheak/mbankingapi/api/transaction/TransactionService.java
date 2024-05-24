package com.vicheak.mbankingapi.api.transaction;

import com.vicheak.mbankingapi.api.transaction.web.TransactionDto;
import com.vicheak.mbankingapi.api.transaction.web.TransactionResponseDto;

import java.util.List;

public interface TransactionService {

    /**
     * This method is used to do a transaction between two accounts
     * @param transactionDto is the request dto from client
     */
    void doTransaction(TransactionDto transactionDto);

    /**
     * This method is used to load all transaction history including transfer and payment
     * @return List<TransactionResponseDto>
     */
    List<TransactionResponseDto> loadAllTransactions();

}
