package com.example.assesment.entities;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Actual filename user uploaded
    @Column(nullable = false)
    private String originalFilename;

    // Server-side stored path
    @Column(nullable = false, unique = true)
    private String storedFilename;

    @Column(nullable = false)
    private long fileSize;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    public Document(String originalFilename, String storedFilename, long fileSize) {
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.fileSize = fileSize;
        this.createdAt = OffsetDateTime.now();
    }

}
