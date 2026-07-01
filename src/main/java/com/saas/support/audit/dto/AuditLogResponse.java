package com.saas.support.audit.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AuditLogResponse {

    private UUID id;
    private UUID userId;
    private String action;
    private String resource;
    private UUID resourceId;
    private String oldValues;
    private String newValues;
    private String ipAddress;
    private LocalDateTime createdAt;
}