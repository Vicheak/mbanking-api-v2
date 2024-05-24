package com.vicheak.mbankingapi.api.account;

import com.vicheak.mbankingapi.api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, UserAccountId> {

    List<UserAccount> findByIdUser(User user);

    List<UserAccount> findByIdAccountNumber(String uuid);

    List<UserAccount> findByIdUserUuid(String userUuid);

    Optional<UserAccount> findByIdUserUuidAndIdAccountNumber(String userUuid, String accountUuid);

    UserAccount findByIdAccount(Account account);

}
