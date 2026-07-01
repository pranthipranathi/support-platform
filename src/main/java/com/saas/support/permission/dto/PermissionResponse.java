package com.saas.support.permission.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PermissionResponse {

    private UUID id;
    private String name;
    private String resource;
    private String action;
    private String description;
}