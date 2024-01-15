package com.vicheak.mbankingapi.api.auth;

import com.vicheak.mbankingapi.api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("UPDATE User AS u SET u.verifiedCode = :verifiedCode WHERE u.email = :email")
    void updateVerificationCode(String email, String verifiedCode);

    boolean existsByEmailAndIsVerifiedTrue(String email);

    Optional<User> findByEmailAndVerifiedCodeAndIsDeletedFalse(String email, String verifiedCode);

}
