package com.vicheak.mbankingapi.security;

import com.vicheak.mbankingapi.api.user.User;
import com.vicheak.mbankingapi.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User authenticatedUser = userRepository.findByUsernameAndIsVerifiedTrueAndIsDeletedFalse(username)
                .orElseThrow(
                        () -> {
                            log.error("Username does not exist in the system!");
                            return new UsernameNotFoundException("Username does not exist in the system!");
                        }
                );

        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUser(authenticatedUser);

//        log.info("Auth Authorities : {}", customUserDetails.getAuthorities());

        return customUserDetails;
    }

}
