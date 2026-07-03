package com.saas.support.admin.controller;

import com.saas.support.admin.dto.AdminStatsResponse;
import com.saas.support.admin.service.AdminService;
import com.saas.support.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Portal", description = "Admin Portal APIs")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/stats")
    @Operation(summary = "Get system-wide statistics")
    public ResponseEntity<ApiResponse<AdminStatsResponse>> getSystemStats() {
        return ResponseEntity.ok(
                ApiResponse.success(adminService.getSystemStats()));
    }
}