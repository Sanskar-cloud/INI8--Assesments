package com.example.assesment.controllers;


import com.example.assesment.dtos.BaseApiResponse;
import com.example.assesment.dtos.DocumentResponseDto;
import com.example.assesment.service.DocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    /**
     * POST /documents/upload - Upload PDF
     */
    @PostMapping("/upload")
    public ResponseEntity<BaseApiResponse<DocumentResponseDto>> upload(@RequestParam("file") MultipartFile file) {
        DocumentResponseDto dto = service.upload(file);
        return ResponseEntity.ok(
                BaseApiResponse.success("File uploaded successfully", dto)
        );
    }

    /**
     * GET /documents - list all documents
     */
    @GetMapping
    public ResponseEntity<BaseApiResponse<List<DocumentResponseDto>>> listAll() {
        return ResponseEntity.ok(
                BaseApiResponse.success("Documents fetched successfully", service.listAll())
        );
    }

    /**
     * GET /documents/{id} - download file
     */
    @GetMapping("/{id}")
    public ResponseEntity<Resource> download(@PathVariable("id") Long id) {
        Resource resource = service.loadAsResource(id);

        String filename = resource.getFilename();
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(encodedFilename).build().toString())
                .body(resource);
    }

    /**
     * DELETE /documents/{id} - delete file & metadata
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseApiResponse<Void>> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.ok(
                BaseApiResponse.success("Document deleted successfully", null)
        );
    }

}
