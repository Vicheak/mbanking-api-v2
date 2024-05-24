package com.vicheak.mbankingapi.api.transaction;

import com.vicheak.mbankingapi.api.transaction.web.TransactionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    Transaction fromTransactionDtoToTransaction(TransactionDto transactionDto);

}
