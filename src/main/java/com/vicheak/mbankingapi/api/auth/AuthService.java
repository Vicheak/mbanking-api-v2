package com.vicheak.mbankingapi.api.auth;

import com.vicheak.mbankingapi.api.auth.web.RegisterDto;
import jakarta.mail.MessagingException;

public interface AuthService {

    /**
     * This method is used to register a customer from client application
     * @param registerDto is the request from client
     */
    void register(RegisterDto registerDto) throws MessagingException;

}
