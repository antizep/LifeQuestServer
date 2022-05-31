package ru.antizep.lifequestserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.antizep.lifequestserver.entity.ScheduleEntity;
import ru.antizep.lifequestserver.entity.UserEntity;
import ru.antizep.lifequestserver.repository.ScheduleRepository;

import java.sql.Time;

@SpringBootTest
class LifeQuestServerApplicationTests {
    @Autowired
    ScheduleRepository repository;


    @Test
    void contextLoads() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(2);

        ScheduleEntity entity = new ScheduleEntity();
        entity.setTime(new Time(90));
        entity.setRejected(0);
        entity.setHeader("");
        entity.setDone(0);
        entity.setUserEntity(userEntity);
        entity.setDayOfWeek("");
        entity.setDescription("e");
        entity.setDaily(false);

        repository.save(entity);
    }

}
