package com.vicheak.mbankingapi.api.account;

import com.vicheak.mbankingapi.api.account.web.CreateAccountDto;
import com.vicheak.mbankingapi.api.account.web.RenameAccountDto;

public interface AccountService {

    /**
     * This method is used to create new account resource into the system
     * @param createAccountDto is the request from client
     */
    void createNewAccount(CreateAccountDto createAccountDto);

    /**
     * This method is used to rename an existing account resource by uuid
     * @param uuid is the path parameter from client
     * @param renameAccountDto is the request from client
     */
    void renameAccountByUuid(String uuid, RenameAccountDto renameAccountDto);

}
