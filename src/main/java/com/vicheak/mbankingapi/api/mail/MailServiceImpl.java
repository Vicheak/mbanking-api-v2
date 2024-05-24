package com.vicheak.mbankingapi.api.mail;

import com.vicheak.mbankingapi.api.mail.web.Mail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendMail(Mail<?> mail) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setSubject(mail.getSubject());
        messageHelper.setFrom(mail.getSender());
        messageHelper.setTo(mail.getReceiver());
        messageHelper.setText(mail.getText().formatted(mail.getMetaData()), true);

        javaMailSender.send(mimeMessage);
    }

}
