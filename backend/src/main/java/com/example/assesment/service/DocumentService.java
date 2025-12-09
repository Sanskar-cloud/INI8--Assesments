package com.example.assesment.service;

import com.example.assesment.dtos.DocumentResponseDto;
import com.example.assesment.entities.Document;
import com.example.assesment.exceptions.BadRequestException;
import com.example.assesment.exceptions.NotFoundException;
import com.example.assesment.repository.DocumentRepository;
import com.example.assesment.storage.FileStorage;
import com.example.assesment.utils.DocumentMapper;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public class DocumentService {

    private static final String PDF_CONTENT_TYPE = "application/pdf";
    private static final String PDF_EXTENSION = "pdf";

    private final DocumentRepository repository;
    private final FileStorage fileStorage;

    public DocumentService(DocumentRepository repository, FileStorage fileStorage) {
        this.repository = repository;
        this.fileStorage = fileStorage;
    }

    public DocumentResponseDto upload(MultipartFile file) {
        validateFile(file);

        String storedFilename;
        try {
            storedFilename = fileStorage.store(file, PDF_EXTENSION);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }

        Document document = new Document(
                file.getOriginalFilename(),
                storedFilename,
                file.getSize()
        );

        Document saved = repository.save(document);
        return DocumentMapper.toDto(saved);
    }

    public List<DocumentResponseDto> listAll() {
        return repository.findAll()
                .stream()
                .map(DocumentMapper::toDto)
                .toList();
    }

    public Resource loadAsResource(Long id) {
        Document document = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Document not found with id " + id));
        return fileStorage.loadAsResource(document.getStoredFilename());
    }

    public void delete(Long id) {
        Document document = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Document not found with id " + id));

        try {
            fileStorage.delete(document.getStoredFilename());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file from storage", e);
        }

        repository.delete(document);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File must not be empty");
        }

        String contentType = file.getContentType();
        String originalName = file.getOriginalFilename() != null
                ? file.getOriginalFilename().toLowerCase()
                : "";

        boolean isPdfByType = PDF_CONTENT_TYPE.equalsIgnoreCase(contentType);
        boolean isPdfByName = originalName.endsWith(".pdf");

        if (!isPdfByType && !isPdfByName) {
            throw new BadRequestException("Only PDF files are allowed");
        }
    }
}

