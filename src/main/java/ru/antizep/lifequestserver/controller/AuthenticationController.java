package ru.antizep.lifequestserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.antizep.lifequestserver.config.MapperUtil;
import ru.antizep.lifequestserver.dto.UserInfoDTO;
import ru.antizep.lifequestserver.entity.UserEntity;
import ru.antizep.lifequestserver.repository.UserRepository;

@Slf4j
@RestController
@RequestMapping("/profile")
public class AuthenticationController {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;
    private final BCryptPasswordEncoder crypt =  new BCryptPasswordEncoder();

    @Autowired
    public AuthenticationController(UserRepository userRepository,MapperUtil mapperUtil) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
    }

    @GetMapping(value = "/")
    public UserInfoDTO getUser(Authentication authentication) {
        return mapperUtil.getMapper().map(userRepository.getByUsername(authentication.getName()),UserInfoDTO.class);
    }

    @PostMapping("/registration/sendMail")
    public void sendMailCode(String mailAddress){

    }

    @GetMapping("/registration/verifyMail")
    public boolean verifyCode(String mailAddress,int code){
        return true;
    }

    @PostMapping("/registration")
    public UserInfoDTO registration(String username, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEnabled(true);
        userEntity.setUsername(username);
        userEntity.setPassword(crypt.encode(password));
        userEntity.setAuthority("user");

        userRepository.save(userEntity);

        return mapperUtil.getMapper().map(userRepository.getByUsername(username),UserInfoDTO.class);
    }
}
