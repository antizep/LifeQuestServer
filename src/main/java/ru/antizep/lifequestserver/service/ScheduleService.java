package ru.antizep.lifequestserver.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import ru.antizep.lifequestserver.dto.ScheduleDto;
import ru.antizep.lifequestserver.dto.UploadFileResponse;
import ru.antizep.lifequestserver.entity.ScheduleEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

public interface ScheduleService {

    List<ScheduleDto> loadSchedule(String username);
    void delete(String username,long id);

    UploadFileResponse uploadFile(String filename, InputStream fileIS, String username, long id);

    ResponseEntity<Resource> downloadFile(String username, String fileName, long id, HttpServletRequest request);

    //todo сдесь вернуть DTO обьект а не сущьность БД.
    ScheduleEntity save(String username, ScheduleDto scheduleDto, String filename, InputStream fileIS);
}
