package com.vicheak.mbankingapi.api.auth;

import com.vicheak.mbankingapi.api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AuthRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("UPDATE User AS u SET u.verifiedCode = :verifiedCode WHERE u.email = :email")
    void updateVerificationCode(String email, String verifiedCode);

}
