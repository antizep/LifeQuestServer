package ru.antizep.lifequestserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.antizep.lifequestserver.config.MapperUtil;
import ru.antizep.lifequestserver.dto.UserInfoDTO;
import ru.antizep.lifequestserver.entity.UserEntity;
import ru.antizep.lifequestserver.repository.MailCodeRepository;
import ru.antizep.lifequestserver.repository.UserRepository;

// todo покрыть микросервис тестами
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final MailCodeRepository mailCodeRepository;
    private final BCryptPasswordEncoder crypt =  new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;

    @Override
    public UserInfoDTO registration(String username, String password) {
        var code = mailCodeRepository.getById(username);

        if(code.isVerify()) {
            UserEntity userEntity = new UserEntity();
            userEntity.setEnabled(true);
            userEntity.setUsername(username);
            userEntity.setPassword(crypt.encode(password));
            userEntity.setAuthority("user");

            userRepository.save(userEntity);
            mailCodeRepository.delete(code);
        }
        return mapperUtil.getMapper().map(userRepository.getByUsername(username),UserInfoDTO.class);
    }
    public UserInfoDTO getUserInfo(String username){
        return mapperUtil.getMapper().map(userRepository.getByUsername(username),UserInfoDTO.class);
    }
}
