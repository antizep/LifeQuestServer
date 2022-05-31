package ru.antizep.lifequestserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.antizep.lifequestserver.config.FileStorageProperties;
import ru.antizep.lifequestserver.exceptions.FileStorageException;
import ru.antizep.lifequestserver.exceptions.MyFileNotFoundException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


// todo покрыть микросервис тестами
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService{

    private Path fileStorageLocation;
    private final FileStorageProperties fileStorageProperties;

    @PostConstruct
    private void init() {

        try {
            fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                    .toAbsolutePath().normalize();
            Files.createDirectories(fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public String storeFile(String filename, InputStream fileIS, long userId, long cheduleId) {

        String fileName = StringUtils.cleanPath(filename);
        Path location = fileStorageLocation.resolve(String.valueOf(userId));
        try {

            if (Files.notExists(location)) {
                Files.createDirectory(location);
            }
            location = location.resolve(String.valueOf(cheduleId));

            if (Files.notExists(location)) {
                Files.createDirectory(location);
            }
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = location.resolve(fileName);
            Files.copy(fileIS, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName,long userId, long cheduleId) {
        try {
            Path location = fileStorageLocation.resolve(String.valueOf(userId));
            location = location.resolve(String.valueOf(cheduleId));
            Path filePath = location.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

}
