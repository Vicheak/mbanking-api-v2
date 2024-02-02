package com.vicheak.mbankingapi.init;

import com.vicheak.mbankingapi.api.authority.Authority;
import com.vicheak.mbankingapi.api.authority.AuthorityRepository;
import com.vicheak.mbankingapi.api.authority.Role;
import com.vicheak.mbankingapi.api.authority.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;

    //@PostConstruct
    public void init() {
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
    }

}
