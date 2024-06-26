package com.vicheak.mbankingapi.api.auth;

import com.vicheak.mbankingapi.api.auth.web.*;
import com.vicheak.mbankingapi.api.mail.web.Mail;
import com.vicheak.mbankingapi.api.mail.MailService;
import com.vicheak.mbankingapi.api.user.User;
import com.vicheak.mbankingapi.api.user.UserMapper;
import com.vicheak.mbankingapi.api.user.UserServiceImpl;
import com.vicheak.mbankingapi.api.user.web.CreateUserDto;
import com.vicheak.mbankingapi.api.user.web.UserDto;
import com.vicheak.mbankingapi.security.CustomUserDetails;
import com.vicheak.mbankingapi.util.RandomUtil;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final AuthMapper authMapper;
    private final UserServiceImpl userService;
    private final MailService mailService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;

    @Value("${spring.mail.username}")
    private String adminMail;

    @Transactional
    @Override
    public void register(RegisterDto registerDto) throws MessagingException {
        CreateUserDto createUserDto = authMapper.fromRegisterDtoToCreateUserDto(registerDto);

        //check user validation
        userService.checkUserValidation(createUserDto);

        //set up new user
        User newUser = userService.setupUser(createUserDto);
        newUser.setIsVerified(false);

        authRepository.save(newUser);

        //check valid role
        userService.checkValidRoles(Set.of(3));

        //set up user roles
        userService.setUpUserRoles(Set.of(3), newUser);

        //generate random six-digit verification code
        String sixDigitCode = RandomUtil.getRandomSixDigit();

        //update random six-digit verification code for the new user
        authRepository.updateVerificationCode(newUser.getEmail(), sixDigitCode);

        //send mail to mailbox
        buildMail(newUser.getEmail(), sixDigitCode);
    }

    @Transactional
    @Override
    public void sendVerification(SendVerifyDto sendVerifyDto) throws MessagingException {
        //generate random six-digit verification code
        String sixDigitCode = RandomUtil.getRandomSixDigit();

        //update random six-digit verification code for the new user
        authRepository.updateVerificationCode(sendVerifyDto.email(), sixDigitCode);

        //send mail to mailbox
        buildMail(sendVerifyDto.email(), sixDigitCode);
    }

    @Transactional
    @Override
    public void verify(VerifyDto verifyDto) {
        //check if the account is already verified
        if (authRepository.existsByEmailAndIsVerifiedTrue(verifyDto.email()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Your account is already verified!");

        //load the verified account
        User toVerifiedUser = authRepository.findByEmailAndVerifiedCodeAndIsDeletedFalse(
                        verifyDto.email(), verifyDto.verificationCode())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Email verification has been failed!")
                );

        toVerifiedUser.setIsVerified(true);
        toVerifiedUser.setVerifiedCode(null);

        authRepository.save(toVerifiedUser);
    }

    @Transactional
    @Override
    public void login(LoginDto loginDto) {
        Authentication auth = new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password());
        //authenticate with authentication provider bean
        auth = authenticationProvider.authenticate(auth);
    }

    @Transactional
    @Override
    public void changePassword(ChangePasswordDto changePasswordDto) {
        //check if auth user matches with the email
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
        User authUser = customUserDetails.getUser();

        //check if the old password matches with the encrypted password
        if (!authUser.getEmail().equals(changePasswordDto.email()) ||
                !passwordEncoder.matches(changePasswordDto.oldPassword(), authUser.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Failed to change account password!");

        //encrypt a new password and update to the database
        authUser.setPassword(passwordEncoder.encode(changePasswordDto.newPassword()));

        //save account into database
        authRepository.save(authUser);
    }

    @Override
    public UserDto viewProfile(Authentication authentication) {
        if (Objects.nonNull(authentication)) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return userMapper.fromUserToUserDto(customUserDetails.getUser());
        }
        return null;
    }

    private void buildMail(String email, String sixDigitCode) throws MessagingException {
        Mail<String> mail = new Mail<>();
        mail.setSubject("Email Verification");
        mail.setSender(adminMail);
        mail.setReceiver(email);
        mail.setText("<h1>Please copy this verification code to verify your account : %s</h1>");
        mail.setMetaData(sixDigitCode);

        mailService.sendMail(mail);
    }

}
