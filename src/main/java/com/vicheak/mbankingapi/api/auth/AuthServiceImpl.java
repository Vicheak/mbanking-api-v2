package com.vicheak.mbankingapi.api.auth;

import com.vicheak.mbankingapi.api.auth.web.RegisterDto;
import com.vicheak.mbankingapi.api.user.User;
import com.vicheak.mbankingapi.api.user.UserServiceImpl;
import com.vicheak.mbankingapi.api.user.web.CreateUserDto;
import com.vicheak.mbankingapi.util.RandomUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final AuthMapper authMapper;
    private final UserServiceImpl userService;
    private final JavaMailSender javaMailSender;

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

        //send mail to mail box
        //build mime message object
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setSubject("Email Verification");
        messageHelper.setFrom(adminMail);
        messageHelper.setTo(newUser.getEmail());
        messageHelper.setText("<h1>Please copy this verification code to verify your account : %s</h1>"
                .formatted(sixDigitCode), true);

        javaMailSender.send(mimeMessage);
    }

}
