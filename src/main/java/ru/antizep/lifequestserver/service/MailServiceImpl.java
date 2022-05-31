package ru.antizep.lifequestserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.antizep.lifequestserver.entity.MailCodeEntity;
import ru.antizep.lifequestserver.repository.MailCodeRepository;

import javax.mail.MessagingException;
import java.util.Random;

// todo покрыть микросервис тестами
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender emailSender;
    private final MailCodeRepository mailCodeRepository;

    @Override
    public void sendMailCode(String mailAddress) throws MessagingException {
        var message = emailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        var code = new Random().nextInt(999999);
        MailCodeEntity codeEntity = new MailCodeEntity();
        codeEntity.setCode(code);
        codeEntity.setMail(mailAddress);
        mailCodeRepository.save(codeEntity);
        messageHelper.setFrom("admin@antizep.ru");
        messageHelper.setTo(mailAddress);
        messageHelper.setSubject("verification code from \"clay\" app");
        messageHelper.setText("verification code from \"clay\" app: "+ code);

        emailSender.send(message);
    }

    @Override
    public boolean verifyCode(String mailAddress, int code) {

        var codeEntity = mailCodeRepository.getById(mailAddress);
        if(code == codeEntity.getCode()){
            codeEntity.setVerify(true);
            mailCodeRepository.save(codeEntity);
            return true;
        }
        return false;

    }
}
