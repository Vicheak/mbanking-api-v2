package com.vicheak.mbankingapi.api.mail;

import com.vicheak.mbankingapi.api.mail.web.Mail;
import jakarta.mail.MessagingException;

public interface MailService {

    /**
     * This method is used to send mail to mailbox
     * @param mail is the request from client
     */
    void sendMail(Mail<?> mail) throws MessagingException;

}
