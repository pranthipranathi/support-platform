package com.saas.support.audit.controller;

import com.saas.support.audit.dto.AuditLogResponse;
import com.saas.support.audit.service.AuditLogService;
import com.saas.support.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
@Tag(name = "Audit Logs", description = "Audit Log APIs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get audit logs by user")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getLogsByUser(
            @PathVariable UUID userId) {
        return ResponseEntity.ok(
                ApiResponse.success(auditLogService.getLogsByUser(userId)));
    }

    @GetMapping("/resource/{resource}/{resourceId}")
    @Operation(summary = "Get audit logs by resource")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getLogsByResource(
            @PathVariable String resource,
            @PathVariable UUID resourceId) {
        return ResponseEntity.ok(
                ApiResponse.success(auditLogService.getLogsByResource(resource, resourceId)));
    }
}