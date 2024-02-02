package com.vicheak.mbankingapi.api.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNumberIgnoreCase(String phoneNumber);

    boolean existsByOneSignalIdIgnoreCase(String oneSignalId);

    boolean existsByStudentCardNoIgnoreCase(String studentCardNo);

    List<User> findAllByIsDeleted(Boolean isDeleted);

    Optional<User> findByUuid(String uuid);

    Optional<User> findByUsernameAndIsVerifiedTrueAndIsDeletedFalse(String username);

}
