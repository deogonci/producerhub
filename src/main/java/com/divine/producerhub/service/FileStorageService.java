package com.divine.producerhub.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of(".mp3", ".wav", ".m4a", ".ogg");

    private final Path uploadDirectory =
            Path.of("uploads").toAbsolutePath().normalize();

    public FileStorageService() {
        try {
            Files.createDirectories(uploadDirectory);
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Could not create the uploads directory",
                    exception
            );
        }
    }

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() == null
                        ? ""
                        : file.getOriginalFilename()
        );

        int dotPosition = originalFilename.lastIndexOf('.');

        if (dotPosition == -1) {
            throw new IllegalArgumentException(
                    "The audio file must have an extension"
            );
        }

        String extension = originalFilename
                .substring(dotPosition)
                .toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException(
                    "Only MP3, WAV, M4A and OGG files are allowed"
            );
        }

        String storedFilename = UUID.randomUUID() + extension;
        Path destination = uploadDirectory.resolve(storedFilename).normalize();

        if (!destination.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException("Invalid file location");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(
                    inputStream,
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Could not store the audio file",
                    exception
            );
        }

        return storedFilename;
    }
}