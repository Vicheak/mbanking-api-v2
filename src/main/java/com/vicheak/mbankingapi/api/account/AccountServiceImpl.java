package com.vicheak.mbankingapi.api.account;

import com.vicheak.mbankingapi.api.account.web.*;
import com.vicheak.mbankingapi.security.CustomUserDetails;
import com.vicheak.mbankingapi.security.securitycheck.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final AccountTypeRepository accountTypeRepository;
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;

    @Transactional
    @Override
    public void createNewAccount(CreateAccountDto createAccountDto) {
        //validate account
        checkAccountValidation(createAccountDto);

        Account newAccount = accountMapper.fromCreateAccountDtoToAccount(createAccountDto);
        newAccount.setNumber(UUID.randomUUID().toString());
        //encrypt the account pin
        newAccount.setPin(passwordEncoder.encode(newAccount.getPin()));
        //set up transfer limits
        setUpTransferLimit(newAccount);

        accountRepository.save(newAccount);

        //set up user accounts
        setupUserAccounts(newAccount);
    }

    @Transactional
    @Override
    public void renameAccountByUuid(String uuid, RenameAccountDto renameAccountDto) {
        //check permissions for customer
        if (!securityUtil.checkSecurityContextControl()) {
            if (checkNoneAccountNumber(uuid))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "This process is unauthorized! Permission denied!");
        }

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

    @Transactional
    @Override
    public void updateTransferLimitByUuid(String uuid, TransferLimitAccountDto transferLimitAccountDto) {
        //load account by uuid
        if (!securityUtil.checkSecurityContextControl()) {
            if (checkNoneAccountNumber(uuid))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "This process is unauthorized! Permission denied!");
        }

        Account account = accountRepository.queryAccountByNumber(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Account with uuid, %s has not been found in the system!"
                                        .formatted(uuid))
                );

        account.setTransferLimit(transferLimitAccountDto.transferLimit());

        accountRepository.save(account);
    }

    @Override
    public List<AccountDto> loadAllAccounts() {
        boolean isCustomer = false;
        List<Account> accounts = new ArrayList<>();

        //check for permissions to ban customer
        if (!securityUtil.checkSecurityContextControl()) {
            Authentication auth = securityUtil.loadAuthenticationContext();
            CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();

            List<UserAccount> userAccounts = userAccountRepository.findByIdUser(customUserDetails.getUser());
            userAccounts.forEach(userAccount -> accounts.add(userAccount.getId().getAccount()));
            isCustomer = true;
        }

        return accountMapper.fromAccountToAccountDto(isCustomer ? accounts : accountRepository.findAll());
    }

    @Override
    public AccountDto loadAccountByUuid(String uuid) {
        //check for permissions to ban customer
        if (!securityUtil.checkSecurityContextControl()) {
            if (checkNoneAccountNumber(uuid))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "This process is unauthorized! Permission denied!");
        }

        return accountMapper.fromAccountToAccountDto(accountRepository.queryAccountByNumber(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Account with uuid, %s has not been found in the system!"
                                        .formatted(uuid))
                ));
    }

    @Transactional
    @Override
    public void closeAccountByUuid(String uuid) {
        //check for permissions to ban customer
        if (!securityUtil.checkSecurityContextControl())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Permission denied!");

        List<UserAccount> userAccounts = userAccountRepository.findByIdAccountNumber(uuid);
        UserAccount userAccount = userAccounts.getFirst();
        userAccount.setIsDisabled(true);

        userAccountRepository.save(userAccount);
    }

    @Override
    public List<AccountDetailDto> loadUserAccountsByUuid(String userUuid) {
        List<AccountDetailDto> accountDetailDtoList = new ArrayList<>();

        List<UserAccount> userAccounts = userAccountRepository.findByIdUserUuid(userUuid);

        userAccounts.forEach(userAccount -> {
            AccountDetailDto accountDetailDto = accountMapper.fromAccountToAccountDetailDto(userAccount.getId().getAccount());
            accountDetailDto.setIsDisabled(userAccount.getIsDisabled());
            accountDetailDtoList.add(accountDetailDto);
        });

        return accountDetailDtoList;
    }

    @Override
    public AccountDetailDto loadUserAccountByUuid(String userUuid, String accountUuid) {
        UserAccount userAccount = userAccountRepository.findByIdUserUuidAndIdAccountNumber(userUuid, accountUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with uuid, %s, and account uuid, %s have not been found in the system!"
                                        .formatted(userUuid, accountUuid))
                );
        AccountDetailDto accountDetailDto = accountMapper.fromAccountToAccountDetailDto(userAccount.getId().getAccount());
        accountDetailDto.setIsDisabled(userAccount.getIsDisabled());
        return accountDetailDto;
    }

    private boolean checkNoneAccountNumber(String uuid) {
        Authentication auth = securityUtil.loadAuthenticationContext();
        CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();

        List<UserAccount> userAccounts = userAccountRepository.findByIdUser(customUserDetails.getUser());
        return userAccounts.stream()
                .noneMatch(userAccount -> userAccount.getId().getAccount().getNumber().equals(uuid));
    }

    private void setupUserAccounts(Account account) {
        Authentication auth = securityUtil.loadAuthenticationContext();
        CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();

        UserAccount newUserAccount = new UserAccount();

        //set up new account for authenticated user
        newUserAccount.setId(UserAccountId.builder()
                .user(customUserDetails.getUser())
                .account(account)
                .build());

        //enable the new account
        newUserAccount.setIsDisabled(false);

        userAccountRepository.save(newUserAccount);
    }

    private void setUpTransferLimit(Account account) {
        switch (account.getAccountType().getName()) {
            case "Checking Account" -> account.setTransferLimit(BigDecimal.valueOf(1000));
            case "Saving Account" -> account.setTransferLimit(BigDecimal.valueOf(2000));
            case "Money Market Account" -> account.setTransferLimit(BigDecimal.valueOf(10000));
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to set up transfer limit for this account!");
        }
    }

    private void checkAccountValidation(CreateAccountDto createAccountDto) {
        //check if account name already exists
        if (accountRepository.existsByNameIgnoreCase(createAccountDto.name()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Account name conflicts resources in the system!");

        //check if account type is valid
        if (!accountTypeRepository.existsById(createAccountDto.accountTypeId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Account type ID is not valid in the system!");

        //check if the user has already had the account type
        checkUserAccountType(createAccountDto.accountTypeId());
    }

    private void checkUserAccountType(Integer accountTypeId) {
        Authentication auth = securityUtil.loadAuthenticationContext();
        CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();

        List<UserAccount> userAccounts = userAccountRepository.findByIdUser(customUserDetails.getUser());
        userAccounts.forEach(userAccount -> {
            if (Objects.equals(userAccount.getId().getAccount().getAccountType().getId(), accountTypeId))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Failed to create new account, look like you already had a related account!");
        });
    }

}
