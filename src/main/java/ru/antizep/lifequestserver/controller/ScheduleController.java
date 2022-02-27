package ru.antizep.lifequestserver.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.antizep.lifequestserver.dto.ScheduleDto;
import ru.antizep.lifequestserver.entity.ScheduleEntity;
import ru.antizep.lifequestserver.entity.UserEntity;
import ru.antizep.lifequestserver.repository.ScheduleRepository;
import ru.antizep.lifequestserver.repository.UserRepository;

import java.sql.Time;

@Slf4j
@RestController
@RequestMapping("/schedule")
@AllArgsConstructor
public class ScheduleController {

    private UserRepository userRepository;
    private ScheduleRepository scheduleRepository;



    @PostMapping(value = "/save")
    public ScheduleEntity save(Authentication authentication,
                               @RequestBody
                                       ScheduleDto scheduleDto ){
        log.info("save user:"+authentication.getName());
        UserEntity user = userRepository.getByUsername(authentication.getName());

        ScheduleEntity entity = new ScheduleEntity();
        entity.setUserEntity(user);
        entity.setDaily(scheduleDto.isDaily());
        entity.setDescription(scheduleDto.getDescription());
        entity.setDone(scheduleDto.getDone());
        entity.setHeader(scheduleDto.getHeader());
        entity.setRejected(scheduleDto.getRejected());
        entity.setTime(Time.valueOf(scheduleDto.getTime()));
        entity.setRemoteId(0);
        entity.setDayOfWeek(scheduleDto.getDayOfWeek());
        return scheduleRepository.save(entity);

    }
}
