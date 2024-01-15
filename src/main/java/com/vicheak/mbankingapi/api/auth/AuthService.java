package com.vicheak.mbankingapi.api.auth;

import com.vicheak.mbankingapi.api.auth.web.RegisterDto;
import com.vicheak.mbankingapi.api.auth.web.SendVerifyDto;
import com.vicheak.mbankingapi.api.auth.web.VerifyDto;
import jakarta.mail.MessagingException;

public interface AuthService {

    /**
     * This method is used to register a customer from client application
     * @param registerDto is the request from client
     */
    void register(RegisterDto registerDto) throws MessagingException;

    /**
     * This method is used to verify customer account by sending email
     * @param sendVerifyDto is the request from client
     */
    void sendVerification(SendVerifyDto sendVerifyDto) throws MessagingException;

    /**
     * This method is used to verify customer account from email
     * @param verifyDto is the request from client
     */
    void verify(VerifyDto verifyDto);

}
