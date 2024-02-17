package com.vicheak.mbankingapi.security.securitycheck;

import com.vicheak.mbankingapi.security.CustomUserDetails;
import com.vicheak.mbankingapi.security.authorityconfig.RoleAuth;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class SecurityUtil {

    public Authentication loadAuthenticationContext(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public void checkSecurityContext() {
        Authentication auth = loadAuthenticationContext();

        //allow admin and manager
        if (auth.getAuthorities().contains(RoleAuth.CUSTOMER.getRoleName()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "This process is unauthorized! Permission denied!");
    }

    public boolean checkSecurityContextControl() {
        Authentication auth = loadAuthenticationContext();
        return auth.getAuthorities().contains(RoleAuth.ADMIN.getRoleName()) ||
                auth.getAuthorities().contains(RoleAuth.MANAGER.getRoleName());
    }

    public boolean checkSecurityContextUpdate(String uuid) {
        Authentication auth = loadAuthenticationContext();
        CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();

        return customUserDetails.getUser().getUuid().equals(uuid);
    }

}
