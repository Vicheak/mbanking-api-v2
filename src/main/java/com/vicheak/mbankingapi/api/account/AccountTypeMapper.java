package com.vicheak.mbankingapi.api.account;

import com.vicheak.mbankingapi.api.account.web.AccountTypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AccountTypeMapper {

    AccountTypeMapper accountTypeMapperInstance = Mappers.getMapper(AccountTypeMapper.class);

    AccountTypeDto fromAccountTypeToAccountTypeDto(AccountType accountType);

    List<AccountTypeDto> fromAccountTypeToAccountTypeDto(List<AccountType> accountTypes);

}
