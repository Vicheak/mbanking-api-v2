package com.vicheak.mbankingapi.api.account;

import com.vicheak.mbankingapi.api.account.web.AccountDto;
import com.vicheak.mbankingapi.api.account.web.CreateAccountDto;
import com.vicheak.mbankingapi.api.account.web.RenameAccountDto;
import com.vicheak.mbankingapi.api.account.web.TransferLimitAccountDto;

import java.util.List;

public interface AccountService {

    /**
     * This method is used to create new account resource into the system from client app
     * @param createAccountDto is the request from client
     */
    void createNewAccount(CreateAccountDto createAccountDto);

    /**
     * This method is used to rename an existing account resource by uuid
     * @param uuid is the path parameter from client
     * @param renameAccountDto is the request from client
     */
    void renameAccountByUuid(String uuid, RenameAccountDto renameAccountDto);

    /**
     * This method is used to update transfer limit of an account by uuid
     * @param uuid is the path parameter from client
     * @param transferLimitAccountDto is the request from client
     */
    void updateTransferLimitByUuid(String uuid, TransferLimitAccountDto transferLimitAccountDto);

    /**
     * This method is used to load all account resources in the system
     * @return List<AccountDto>
     */
    List<AccountDto> loadAllAccounts();

    /**
     * This method is used to load specific account resource by uuid
     * @param uuid is the path parameter from client
     * @return AccountDto
     */
    AccountDto loadAccountByUuid(String uuid);

}
