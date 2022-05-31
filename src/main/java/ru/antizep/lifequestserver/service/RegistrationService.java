package ru.antizep.lifequestserver.service;

import ru.antizep.lifequestserver.dto.UserInfoDTO;

public interface RegistrationService {

    UserInfoDTO registration(String username, String password);
    UserInfoDTO getUserInfo(String username);

}
