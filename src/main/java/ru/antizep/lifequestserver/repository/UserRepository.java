package ru.antizep.lifequestserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.antizep.lifequestserver.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity getByUsername(String username);

}
