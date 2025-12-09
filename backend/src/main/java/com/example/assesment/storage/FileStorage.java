package com.example.assesment.storage;



import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorage {

    String store(MultipartFile file, String extension) throws IOException;

    Resource loadAsResource(String storedFilename);

    void delete(String storedFilename) throws IOException;

    Path resolvePath(String storedFilename);
}
