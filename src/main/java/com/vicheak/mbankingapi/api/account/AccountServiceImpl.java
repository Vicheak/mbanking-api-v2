package com.vicheak.mbankingapi.api.account;

import com.vicheak.mbankingapi.api.account.web.CreateAccountDto;
import com.vicheak.mbankingapi.api.account.web.RenameAccountDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final AccountTypeRepository accountTypeRepository;

    @Transactional
    @Override
    public void createNewAccount(CreateAccountDto createAccountDto) {
        //check if account name already exists
        if (accountRepository.existsByNameIgnoreCase(createAccountDto.name()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Account name conflicts resources in the system!");

        //check if account type is valid
        if (!accountTypeRepository.existsById(createAccountDto.accountTypeId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Account type ID is not valid in the system!");

        Account newAccount = accountMapper.fromCreateAccountDtoToAccount(createAccountDto);
        newAccount.setNumber(UUID.randomUUID().toString());
        //set up transfer limits
        setUpTransferLimit(newAccount);

        accountRepository.save(newAccount);
    }

    @Transactional
    @Override
    public void renameAccountByUuid(String uuid, RenameAccountDto renameAccountDto) {
        //load account by uuid
        Account account = accountRepository.queryAccountByNumber(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Account with uuid, %s has not been found in the system!"
                                        .formatted(uuid))
                );

        account.setName(renameAccountDto.renameTo());

        accountRepository.save(account);
    }

    private void setUpTransferLimit(Account account) {
        if (account.getAccountType().getName().equals("Checking Account"))
            account.setTransferLimit(BigDecimal.valueOf(1000));
        else if (account.getAccountType().getName().equals("Saving Account"))
            account.setTransferLimit(BigDecimal.valueOf(2000));
        else
            account.setTransferLimit(BigDecimal.valueOf(10000));
    }

}
