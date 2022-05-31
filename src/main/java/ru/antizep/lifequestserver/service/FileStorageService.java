package ru.antizep.lifequestserver.service;

import org.springframework.core.io.Resource;

import java.io.InputStream;

public interface FileStorageService {

    String storeFile(String filename, InputStream fileIS, long userId, long cheduleId);

    Resource loadFileAsResource(String fileName, long userId, long cheduleId);
}
