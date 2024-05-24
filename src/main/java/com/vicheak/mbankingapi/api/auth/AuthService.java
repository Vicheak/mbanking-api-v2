package com.vicheak.mbankingapi.api.auth;

import com.vicheak.mbankingapi.api.auth.web.*;
import com.vicheak.mbankingapi.api.user.web.UserDto;
import jakarta.mail.MessagingException;
import org.springframework.security.core.Authentication;

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

    /**
     * This method is used to log in a customer from client application
     * @param loginDto is the request from client
     */
    void login(LoginDto loginDto);

    /**
     * This method is used to change user password by specifying the previous password
     * @param changePasswordDto is the request from client
     */
    void changePassword(ChangePasswordDto changePasswordDto);

    /**
     * This method is used to retrieve the logged in customer profile
     * @param authentication is the auth request from client
     * @return UserDto
     */
    UserDto viewProfile(Authentication authentication);

}
