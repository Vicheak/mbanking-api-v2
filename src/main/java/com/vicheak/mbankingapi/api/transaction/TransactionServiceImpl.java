package com.vicheak.mbankingapi.api.transaction;

import com.vicheak.mbankingapi.api.account.Account;
import com.vicheak.mbankingapi.api.account.AccountRepository;
import com.vicheak.mbankingapi.api.account.UserAccount;
import com.vicheak.mbankingapi.api.account.UserAccountRepository;
import com.vicheak.mbankingapi.api.transaction.web.TransactionDto;
import com.vicheak.mbankingapi.api.transaction.web.TransactionReceiverResponseDto;
import com.vicheak.mbankingapi.api.transaction.web.TransactionResponseDto;
import com.vicheak.mbankingapi.api.transaction.web.TransactionSenderResponseDto;
import com.vicheak.mbankingapi.security.CustomUserDetails;
import com.vicheak.mbankingapi.security.securitycheck.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountRepository accountRepository;
    private final UserAccountRepository userAccountRepository;
    private final SecurityUtil securityUtil;

    @Transactional
    @Override
    public void doTransaction(TransactionDto transactionDto) {
        //check valid authenticated sender account
        Account senderAccount = accountRepository.queryAccountByNumber(transactionDto.senderAccountNumber())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Sender account have not been found in the system!")
                );
        UserAccount userAccount = userAccountRepository.findByIdAccount(senderAccount);
        if (!securityUtil.checkSecurityContextUpdate(userAccount.getId().getUser().getUuid()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "You are unauthorized to perform this transaction!");

        //check valid sender and receiver
        if (transactionDto.senderAccountNumber().equals(transactionDto.receiverAccountNumber()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Sender and receiver account cannot be the same!");

        //load sender and receiver account
        Account receiverAccount = accountRepository.queryAccountByNumber(transactionDto.receiverAccountNumber())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Receiver account have not been found in the system!")
                );

        //check sender account amount
        BigDecimal transactionAmount = Objects.isNull(senderAccount.getAmount()) ?
                BigDecimal.ZERO : senderAccount.getAmount();
        if (transactionAmount.doubleValue() < transactionDto.amount().doubleValue())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Insufficient amount! Transaction cannot be completed!");

        //check sender account transfer limit
        if (!transactionDto.isPayment() && (transactionDto.amount().doubleValue() > senderAccount.getTransferLimit().doubleValue()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You cannot transfer amount over account limit transfer, %.2f$"
                            .formatted(senderAccount.getTransferLimit().doubleValue()));

        //perform transaction on sender and receiver account
        senderAccount.setAmount(transactionAmount.subtract(transactionDto.amount()));
        BigDecimal receiverAmount = Objects.isNull(receiverAccount.getAmount()) ?
                BigDecimal.ZERO : receiverAccount.getAmount();
        receiverAccount.setAmount(receiverAmount.add(transactionDto.amount()));

        accountRepository.saveAll(List.of(senderAccount, receiverAccount));

        Transaction transaction = transactionMapper.fromTransactionDtoToTransaction(transactionDto);
        transaction.setUuid(UUID.randomUUID().toString());
        transaction.setSender(senderAccount);
        transaction.setReceiver(receiverAccount);
        transaction.setTransactionAt(LocalDateTime.now());

        transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionResponseDto> loadAllTransactions() {
        //load authenticated user
        CustomUserDetails customUserDetails =
                (CustomUserDetails) securityUtil.loadAuthenticationContext().getPrincipal();

        List<UserAccount> userAccounts = userAccountRepository.findByIdUser(customUserDetails.getUser());

        List<Transaction> senderTransactions = new ArrayList<>();
        userAccounts.forEach(userAccount -> {
            Account account = userAccount.getId().getAccount();
            senderTransactions.addAll(transactionRepository.findBySender(account));
        });

        List<TransactionResponseDto> transactionResponseDtoList = new ArrayList<>();
        senderTransactions.forEach(senderTransaction -> {
            transactionResponseDtoList.add(TransactionResponseDto.builder()
                    .uuid(senderTransaction.getUuid())
                    .transactionSender(TransactionSenderResponseDto.builder()
                            .senderAccountNumber(senderTransaction.getSender().getNumber())
                            .senderAccountName(senderTransaction.getSender().getName())
                            .senderAccountType(senderTransaction.getSender().getAccountType().getName())
                            .build())
                    .transactionReceiver(TransactionReceiverResponseDto.builder()
                            .receiverAccountNumber(senderTransaction.getReceiver().getNumber())
                            .receiverAccountName(senderTransaction.getReceiver().getName())
                            .receiverAccountType(senderTransaction.getReceiver().getAccountType().getName())
                            .build())
                    .amount(senderTransaction.getAmount())
                    .remark((Objects.isNull(senderTransaction.getRemark()) ||
                            senderTransaction.getRemark().isEmpty()) ? "No Remark Message" :
                            senderTransaction.getRemark())
                    .transactionAt(senderTransaction.getTransactionAt())
                    .isPayment(senderTransaction.getIsPayment())
                    .build());
        });

        return transactionResponseDtoList;
    }

}
