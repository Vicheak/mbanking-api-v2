package com.vicheak.mbankingapi.api.account;

import com.vicheak.mbankingapi.api.account.web.AccountTypeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountTypeServiceImpl implements AccountTypeService {

    private final AccountTypeRepository accountTypeRepository;

    @Override
    public List<AccountTypeDto> loadAllAccountTypes() {
        return AccountTypeMapper.accountTypeMapperInstance
                .fromAccountTypeToAccountTypeDto(accountTypeRepository.findAll());
    }

    @Override
    public AccountTypeDto loadAccountTypeById(Integer id) {
        return AccountTypeMapper.accountTypeMapperInstance
                .fromAccountTypeToAccountTypeDto(accountTypeRepository.findById(id)
                        .orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "Account type with id, %d has not been found in the system!"
                                                .formatted(id))
                        ));
    }

    public AccountType findAccountTypeById(Integer id) {
        return accountTypeRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Account type with id, %d has not been found in the system!"
                                        .formatted(id))
                );
    }

}
