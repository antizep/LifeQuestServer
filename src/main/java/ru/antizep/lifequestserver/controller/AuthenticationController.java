package ru.antizep.lifequestserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.antizep.lifequestserver.dto.UserInfoDTO;
import ru.antizep.lifequestserver.service.MailService;
import ru.antizep.lifequestserver.service.RegistrationService;

import javax.mail.MessagingException;

//todo перейти на библиотеку springdoc-openapi вместо сваггера, затем добавить описание методов
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class AuthenticationController {
    private final MailService mailService;
    private final RegistrationService registrationService;


    @GetMapping(value = "/")
    public UserInfoDTO getUser(Authentication authentication) {
        return registrationService.getUserInfo(authentication.getName());
    }

    @PostMapping("/registration/sendMail")
    public void sendMailCode(String mailAddress) throws MessagingException {
        mailService.sendMailCode(mailAddress);
    }

    @GetMapping("/registration/verifyMail")
    public boolean verifyCode(String mailAddress,int code){
        return mailService.verifyCode(mailAddress,code);
    }

    @PostMapping("/registration")
    public UserInfoDTO registration(String username, String password) {
        return registrationService.registration(username, password);
    }
}
