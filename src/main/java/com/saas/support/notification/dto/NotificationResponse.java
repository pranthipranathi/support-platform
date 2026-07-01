package com.saas.support.notification.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationResponse {

    private UUID id;
    private UUID userId;
    private String type;
    private String title;
    private String message;
    private boolean isRead;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;
}