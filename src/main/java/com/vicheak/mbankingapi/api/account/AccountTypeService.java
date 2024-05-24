package com.vicheak.mbankingapi.api.account;

import com.vicheak.mbankingapi.api.account.web.AccountTypeDto;

import java.util.List;

public interface AccountTypeService {

    /**
     * This method is used to retrieve all account type resources
     * @return List<AccountTypeDto>
     */
    List<AccountTypeDto> loadAllAccountTypes();

    /**
     * This method is used to retrieve account type resource by type id
     * @param id is the path parameter from client
     * @return AccountTypeDto
     */
    AccountTypeDto loadAccountTypeById(Integer id);

}
