package com.vicheak.mbankingapi.api.transaction;

import com.vicheak.mbankingapi.api.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySender(Account sender);
    List<Transaction> findByReceiver(Account receiver);

}
