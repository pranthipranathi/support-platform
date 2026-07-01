package com.saas.support.permission.controller;

import com.saas.support.common.response.ApiResponse;
import com.saas.support.permission.dto.PermissionResponse;
import com.saas.support.permission.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@Tag(name = "Permissions", description = "Permission Management APIs")
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    @Operation(summary = "Get all permissions")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAllPermissions() {
        return ResponseEntity.ok(
                ApiResponse.success(permissionService.getAllPermissions()));
    }

    @GetMapping("/resource/{resource}")
    @Operation(summary = "Get permissions by resource")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getByResource(
            @PathVariable String resource) {
        return ResponseEntity.ok(
                ApiResponse.success(permissionService.getPermissionsByResource(resource)));
    }
}