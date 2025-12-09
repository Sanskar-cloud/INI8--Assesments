package com.example.assesment.dtos;


import java.time.OffsetDateTime;

public class DocumentResponseDto {

    private Long id;
    private String filename;
    private long size;
    private OffsetDateTime createdAt;

    public DocumentResponseDto(Long id, String filename, long size, OffsetDateTime createdAt) {
        this.id = id;
        this.filename = filename;
        this.size = size;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public long getSize() {
        return size;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}

