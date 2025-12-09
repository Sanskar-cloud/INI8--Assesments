package com.example.assesment.utils;


import com.example.assesment.dtos.DocumentResponseDto;
import com.example.assesment.entities.Document;
public final class DocumentMapper {

    private DocumentMapper() {}

    public static DocumentResponseDto toDto(Document document) {
        return new DocumentResponseDto(
                document.getId(),
                document.getOriginalFilename(),
                document.getFileSize(),
                document.getCreatedAt()
        );
    }
}
