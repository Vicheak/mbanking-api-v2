package com.vicheak.mbankingapi.init;

import com.vicheak.mbankingapi.api.account.AccountType;
import com.vicheak.mbankingapi.api.account.AccountTypeRepository;
import com.vicheak.mbankingapi.api.authority.Authority;
import com.vicheak.mbankingapi.api.authority.AuthorityRepository;
import com.vicheak.mbankingapi.api.authority.Role;
import com.vicheak.mbankingapi.api.authority.RoleRepository;
import com.vicheak.mbankingapi.api.user.User;
import com.vicheak.mbankingapi.api.user.UserRepository;
import com.vicheak.mbankingapi.api.user.UserRole;
import com.vicheak.mbankingapi.api.user.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        //check if there's already a construction
        if (!authorityRepository.findAll().isEmpty()) return;
        if (!roleRepository.findAll().isEmpty()) return;
        if (!accountTypeRepository.findAll().isEmpty()) return;

        //authorities
        Authority userRead = Authority.builder()
                .name("user:read")
                .build();

        Authority userWrite = Authority.builder()
                .name("user:write")
                .build();

        Authority userUpdate = Authority.builder()
                .name("user:update")
                .build();

        Authority userDelete = Authority.builder()
                .name("user:delete")
                .build();

        Authority userProfile = Authority.builder()
                .name("user:profile")
                .build();

        Authority accountRead = Authority.builder()
                .name("account:read")
                .build();

        Authority accountWrite = Authority.builder()
                .name("account:write")
                .build();

        Authority accountUpdate = Authority.builder()
                .name("account:update")
                .build();

        Authority accountDelete = Authority.builder()
                .name("account:delete")
                .build();

        Authority transactionRead = Authority.builder()
                .name("transaction:read")
                .build();

        Authority transactionWrite = Authority.builder()
                .name("transaction:write")
                .build();

        Authority transactionUpdate = Authority.builder()
                .name("transaction:update")
                .build();

        Authority transactionDelete = Authority.builder()
                .name("transaction:delete")
                .build();

        //roles_authorities
        Set<Authority> fullAuthorities = Set.of(
                userRead, userWrite, userUpdate, userDelete, userProfile,
                accountRead, accountWrite, accountUpdate, accountDelete,
                transactionRead, transactionWrite, transactionUpdate, transactionDelete);

        Set<Authority> managerAuthorities = Set.of(
                userRead, userUpdate, userDelete, userProfile,
                accountRead, accountUpdate, accountDelete,
                transactionRead, transactionUpdate, transactionDelete);

        Set<Authority> customerAuthorities = Set.of(
                userUpdate, userProfile,
                accountRead, accountWrite, accountUpdate,
                transactionRead, transactionWrite);

        //roles
        Role adminRole = Role.builder()
                .name("ADMIN")
                .authorities(fullAuthorities)
                .build();

        Role managerRole = Role.builder()
                .name("MANAGER")
                .authorities(managerAuthorities)
                .build();

        Role customerRole = Role.builder()
                .name("CUSTOMER")
                .authorities(customerAuthorities)
                .build();

        authorityRepository.saveAll(fullAuthorities);
        roleRepository.saveAll(List.of(adminRole, managerRole, customerRole));

        //generate sample data
        //account types
        AccountType checkingAcc = AccountType.builder()
                .name("Checking Account")
                .build();

        AccountType savingAcc = AccountType.builder()
                .name("Saving Account")
                .build();

        AccountType moneyMarketAcc = AccountType.builder()
                .name("Money Market Account")
                .build();

        accountTypeRepository.saveAll(
                new ArrayList<>(List.of(checkingAcc, savingAcc, moneyMarketAcc)));

        //users
        User adminUser = User.builder()
                .email("admin@gmail.com")
                .gender("male")
                .isDeleted(false)
                .isVerified(true)
                .password(passwordEncoder.encode("12345678"))
                .phoneNumber("089434243")
                .username("admin")
                .uuid(UUID.randomUUID().toString())
                .build();

        User managerUser = User.builder()
                .email("manager@gmail.com")
                .gender("male")
                .isDeleted(false)
                .isVerified(true)
                .password(passwordEncoder.encode("12345678"))
                .phoneNumber("089434248")
                .username("manager")
                .uuid(UUID.randomUUID().toString())
                .build();

        User customerUser = User.builder()
                .email("customer@gmail.com")
                .gender("male")
                .isDeleted(false)
                .isVerified(true)
                .password(passwordEncoder.encode("12345678"))
                .phoneNumber("089434246")
                .username("customer")
                .uuid(UUID.randomUUID().toString())
                .build();

        userRepository.saveAll(
                new ArrayList<>(List.of(adminUser, managerUser, customerUser)));

        //user roles
        UserRole adminUserRole = UserRole.builder()
                .user(adminUser)
                .role(adminRole)
                .build();


        UserRole managerUserRole = UserRole.builder()
                .user(managerUser)
                .role(managerRole)
                .build();


        UserRole customerUserRole = UserRole.builder()
                .user(customerUser)
                .role(customerRole)
                .build();

        userRoleRepository.saveAll(
                new ArrayList<>(List.of(adminUserRole, managerUserRole, customerUserRole)));
    }

}
