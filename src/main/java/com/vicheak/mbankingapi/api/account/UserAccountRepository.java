package com.vicheak.mbankingapi.api.account;

import com.vicheak.mbankingapi.api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAccountRepository extends JpaRepository<UserAccount, UserAccountId> {

    List<UserAccount> findByIdUser(User user);

}
