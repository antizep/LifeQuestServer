package ru.antizep.lifequestserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.antizep.lifequestserver.entity.MailCodeEntity;

public interface MailCodeRepository extends JpaRepository<MailCodeEntity,String> {
}
