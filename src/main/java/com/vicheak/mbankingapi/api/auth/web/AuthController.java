package com.vicheak.mbankingapi.api.auth.web;

import com.vicheak.mbankingapi.api.auth.AuthService;
import com.vicheak.mbankingapi.base.BaseApi;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public BaseApi<?> register(@RequestBody @Valid RegisterDto registerDto) throws MessagingException {
        authService.register(registerDto);
        return BaseApi.builder()
                .isSuccess(true)
                .message("Registered successfully!")
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .payload("Please check your email for verification code!")
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/send-verification")
    public BaseApi<?> sendVerification(@RequestBody @Valid SendVerifyDto sendVerifyDto) throws MessagingException {
        authService.sendVerification(sendVerifyDto);
        return BaseApi.builder()
                .isSuccess(true)
                .message("Send verification email successfully!")
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .payload("Please check your email for verification code!")
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verify")
    public BaseApi<?> verify(@RequestBody @Valid VerifyDto verifyDto) {
        authService.verify(verifyDto);
        return BaseApi.builder()
                .isSuccess(true)
                .message("Email verification has been done successfully!")
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .payload("Now you can use your account to login!")
                .build();
    }

}
