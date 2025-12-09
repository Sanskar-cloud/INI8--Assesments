package com.example.assesment.storage.impl;



import com.example.assesment.config.StorageProperties;
import com.example.assesment.exceptions.NotFoundException;
import com.example.assesment.storage.FileStorage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class LocalFileStorage implements FileStorage {

    private final Path rootLocation;

    public LocalFileStorage(StorageProperties properties) {
        this.rootLocation = Path.of(properties.getUploadDir()).toAbsolutePath().normalize();
        init();
    }

    private void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @Override
    public String store(MultipartFile file, String extension) throws IOException {
        String uniqueName = UUID.randomUUID().toString().replace("-", "");
        String filename = uniqueName + "." + extension;
        Path destinationFile = rootLocation.resolve(filename);

        Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }

    @Override
    public Resource loadAsResource(String storedFilename) {
        Path file = rootLocation.resolve(storedFilename);
        if (!Files.exists(file)) {
            throw new NotFoundException("File not found on server");
        }
        return new FileSystemResource(file.toFile());
    }

    @Override
    public void delete(String storedFilename) throws IOException {
        if (!StringUtils.hasText(storedFilename)) return;
        Path file = rootLocation.resolve(storedFilename);
        if (Files.exists(file)) {
            Files.delete(file);
        }
    }

    @Override
    public Path resolvePath(String storedFilename) {
        return rootLocation.resolve(storedFilename);
    }
}
