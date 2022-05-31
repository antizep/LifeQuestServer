package ru.antizep.lifequestserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.antizep.lifequestserver.dto.ScheduleDto;
import ru.antizep.lifequestserver.dto.UploadFileResponse;
import ru.antizep.lifequestserver.entity.ScheduleEntity;
import ru.antizep.lifequestserver.entity.UserEntity;
import ru.antizep.lifequestserver.repository.ScheduleRepository;
import ru.antizep.lifequestserver.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// todo покрыть микросервис тестами
@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final ModelMapper mapperUtil;
    private final FileStorageService fileStorageServiceImpl;


    @Override
    public List<ScheduleDto> loadSchedule(String username) {
        UserEntity entity = userRepository.getByUsername(username);
        var scledule = scheduleRepository.findAllByUserEntity(entity)
                .stream()
                .map(m -> mapperUtil.map(m, ScheduleDto.class))
                .collect(Collectors.toList());
        return scledule;
    }

    @Override
    public void delete(String username, long id) {
        UserEntity entity = userRepository.getByUsername(username);
        log.info("user:" + entity.getUsername() + " delete id:" + id);
        var schedule = scheduleRepository.findByRemoteId(id);
        if (Objects.nonNull(schedule)) {
            if (entity.getId() == schedule.getUserEntity().getId()) {
                scheduleRepository.delete(schedule);
            }
        }
    }

    @Override
    public UploadFileResponse uploadFile(String filename, InputStream fileIS, String username, long id) {
        UserEntity user = userRepository.getByUsername(username);
        String path = "/schedule/" + id + "/";
        String fileName = fileStorageServiceImpl.storeFile(filename,fileIS, user.getId(), id);
        log.info("Upload file " + fileName + " to " + path);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path)
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri);
    }

    @Override
    public ResponseEntity<Resource> downloadFile(String username, String fileName, long id, HttpServletRequest request) {
        UserEntity user = userRepository.getByUsername(username);

        Resource resource = fileStorageServiceImpl.loadFileAsResource(fileName, user.getId(), id);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    //todo сдесь вернуть DTO обьект а не сущьность БД.
    @Override
    public ScheduleEntity save(String username, ScheduleDto scheduleDto, String filename,InputStream fileIS) {
        log.info("save user:" + username);
        UserEntity user = userRepository.getByUsername(username);
        ScheduleEntity entity = new ScheduleEntity();
        entity.setUserEntity(user);
        entity.setDaily(scheduleDto.isDaily());
        entity.setDescription(scheduleDto.getDescription());
        entity.setDone(scheduleDto.getDone());
        entity.setHeader(scheduleDto.getHeader());
        entity.setRejected(scheduleDto.getRejected());
        entity.setTime(Time.valueOf(scheduleDto.getTime()));
        entity.setRemoteId(scheduleDto.getRemoteId());
        entity.setDayOfWeek(scheduleDto.getDayOfWeek());

        entity = scheduleRepository.save(entity);
        if (Objects.nonNull(filename)) {
            uploadFile(filename,fileIS,username, entity.getRemoteId());
        }
        log.info("saved:" + entity.toString());
        entity.setUserEntity(null);
        return entity;
    }
}
