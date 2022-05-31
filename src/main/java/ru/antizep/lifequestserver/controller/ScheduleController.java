package ru.antizep.lifequestserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.antizep.lifequestserver.dto.ScheduleDto;
import ru.antizep.lifequestserver.dto.UploadFileResponse;
import ru.antizep.lifequestserver.entity.ScheduleEntity;
import ru.antizep.lifequestserver.repository.ScheduleRepository;
import ru.antizep.lifequestserver.repository.UserRepository;
import ru.antizep.lifequestserver.service.ScheduleService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
//todo перейти на библиотеку springdoc-openapi вместо сваггера, затем добавить описание методов
@Slf4j
@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    private final ScheduleService scheduleService;

    @PostMapping(value = "/save")
    public ScheduleEntity save(Authentication authentication,
                               @ModelAttribute
                                       ScheduleDto scheduleDto,
                               @RequestPart(value = "file", name = "file", required = false) MultipartFile file) throws IOException {

       return scheduleService.save(authentication.getName(),scheduleDto,file.getOriginalFilename(),file.getInputStream());
    }

    @GetMapping(value = "/findAll")
    public List<ScheduleDto> loadSchedule(Authentication authentication) {
        return scheduleService.loadSchedule(authentication.getName());
    }

    @PostMapping(value = "{id}/delete")
    public void deleteSchedule(Authentication authentication, @PathVariable long id) {
        scheduleService.delete(authentication.getName(), id);
    }

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(Authentication authentication, @RequestPart("file") MultipartFile file, @RequestParam long id) throws IOException {
        return scheduleService.uploadFile(file.getOriginalFilename(),file.getInputStream(),authentication.getName(), id);
    }

    @GetMapping("/{id}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(Authentication authentication, @PathVariable String fileName, @PathVariable long id, HttpServletRequest request) {
        return scheduleService.downloadFile(authentication.getName(), fileName, id, request);
    }
}
