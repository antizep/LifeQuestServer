package ru.antizep.lifequestserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDTO {

    private long id;
    private String username;
    private boolean enabled;
    private String authority;

}
