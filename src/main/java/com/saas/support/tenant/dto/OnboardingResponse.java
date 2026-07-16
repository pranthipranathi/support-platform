package com.saas.support.tenant.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OnboardingResponse {

    private UUID organizationId;
    private UUID tenantId;
    private UUID adminUserId;
    private String schemaName;
    private String organizationName;
    private String slug;
    private String adminEmail;
    private String plan;
    private String message;
}