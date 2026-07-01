package com.saas.support.role.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RoleResponse {

    private UUID id;
    private String name;
    private String displayName;
    private String description;
    private boolean isSystem;
    private boolean isActive;
    private LocalDateTime createdAt;
}