package ru.antizep.lifequestserver.repository;

import org.springframework.data.repository.CrudRepository;
import ru.antizep.lifequestserver.entity.ScheduleEntity;
import ru.antizep.lifequestserver.entity.UserEntity;

import java.util.List;

public interface ScheduleRepository extends CrudRepository<ScheduleEntity,Integer> {
    List<ScheduleEntity> findAllByUserEntity(UserEntity user);
    ScheduleEntity findByRemoteId(long remoteId);
}
