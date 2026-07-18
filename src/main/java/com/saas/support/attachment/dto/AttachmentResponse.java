package com.saas.support.attachment.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AttachmentResponse {

    private UUID id;
    private UUID ticketId;
    private UUID uploadedBy;
    private String fileName;
    private Long fileSize;
    private String mimeType;
    private LocalDateTime createdAt;
}