package ru.antizep.lifequestserver.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.antizep.lifequestserver.dto.ScheduleDto;
import ru.antizep.lifequestserver.dto.UploadFileResponse;
import ru.antizep.lifequestserver.entity.ScheduleEntity;
import ru.antizep.lifequestserver.entity.UserEntity;
import ru.antizep.lifequestserver.repository.ScheduleRepository;
import ru.antizep.lifequestserver.repository.UserRepository;
import ru.antizep.lifequestserver.service.FileStorageService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/schedule")
@AllArgsConstructor
public class ScheduleController {

    private UserRepository userRepository;
    private ScheduleRepository scheduleRepository;
    private FileStorageService fileStorageService;
    private ModelMapper mapperUtil;

    @PostMapping(value = "/save")
    public ScheduleEntity save(Authentication authentication,
                               @ModelAttribute
                                       ScheduleDto scheduleDto,
                               @RequestPart("file") MultipartFile file) {

        log.info("save user:" + authentication.getName());
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

        entity = scheduleRepository.save(entity);
        uploadFile(authentication, file, entity.getRemoteId());

        return entity;
    }

    @GetMapping(value = "/findAll")
    public List<ScheduleDto> loadSchedule(Authentication authentication) {

        UserEntity entity = userRepository.getByUsername(authentication.getName());
        var scledule = scheduleRepository.findAllByUserEntity(entity)
                .stream()
                .map(m -> mapperUtil.map(m, ScheduleDto.class))
                .collect(Collectors.toList());

        return scledule;

    }

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(Authentication authentication, @RequestPart("file") MultipartFile file, @RequestParam long id) {
        UserEntity user = userRepository.getByUsername(authentication.getName());

        String fileName = fileStorageService.storeFile(file, user.getId(), id);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/schedule/" + id + "/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @GetMapping("/{id}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(Authentication authentication, @PathVariable String fileName, @PathVariable long id, HttpServletRequest request) {
        // Load file as Resource
        UserEntity user = userRepository.getByUsername(authentication.getName());

        Resource resource = fileStorageService.loadFileAsResource(fileName, user.getId(), id);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
