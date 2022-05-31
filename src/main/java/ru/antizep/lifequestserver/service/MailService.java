package ru.antizep.lifequestserver.service;

import javax.mail.MessagingException;

public interface MailService {
    void sendMailCode(String mailAddress) throws MessagingException;
    boolean verifyCode(String mailAddress,int code);
}
