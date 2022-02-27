package ru.antizep.lifequestserver.repository;

import org.springframework.data.repository.CrudRepository;
import ru.antizep.lifequestserver.entity.ScheduleEntity;

public interface ScheduleRepository extends CrudRepository<ScheduleEntity,Integer> {
}
