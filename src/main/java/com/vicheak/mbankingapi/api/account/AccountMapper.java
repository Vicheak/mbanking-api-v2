package com.vicheak.mbankingapi.api.account;

import com.vicheak.mbankingapi.api.account.web.CreateAccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = AccountTypeServiceImpl.class)
public interface AccountMapper {

    @Mapping(target = "accountType", source = "accountTypeId")
    Account fromCreateAccountDtoToAccount(CreateAccountDto createAccountDto);

}
