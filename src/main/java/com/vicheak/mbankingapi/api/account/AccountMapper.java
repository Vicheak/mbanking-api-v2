package com.vicheak.mbankingapi.api.account;

import com.vicheak.mbankingapi.api.account.web.AccountDetailDto;
import com.vicheak.mbankingapi.api.account.web.AccountDto;
import com.vicheak.mbankingapi.api.account.web.CreateAccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = AccountTypeServiceImpl.class)
public interface AccountMapper {

    @Mapping(target = "accountType", source = "accountTypeId")
    Account fromCreateAccountDtoToAccount(CreateAccountDto createAccountDto);

    @Mapping(target = "accountType", source = "accountType.name")
    AccountDto fromAccountToAccountDto(Account account);

    List<AccountDto> fromAccountToAccountDto(List<Account> accounts);

    @Mapping(target = "accountType", source = "accountType.name")
    AccountDetailDto fromAccountToAccountDetailDto(Account account);

}
