package com.saas.support.admin.controller;

import com.saas.support.admin.dto.AdminStatsResponse;
import com.saas.support.admin.service.AdminService;
import com.saas.support.common.response.ApiResponse;
import com.saas.support.tenant.dto.OnboardingRequest;
import com.saas.support.tenant.dto.OnboardingResponse;
import com.saas.support.tenant.service.TenantOnboardingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Portal", description = "Admin Portal APIs")
public class AdminController {

    private final AdminService adminService;
    private final TenantOnboardingService tenantOnboardingService;

    @GetMapping("/stats")
    @Operation(summary = "Get system-wide statistics")
    public ResponseEntity<ApiResponse<AdminStatsResponse>> getSystemStats() {
        return ResponseEntity.ok(
                ApiResponse.success(adminService.getSystemStats()));
    }

    @PostMapping("/tenants/onboard")
    @Operation(summary = "Onboard a new organization/tenant")
    public ResponseEntity<ApiResponse<OnboardingResponse>> onboard(
            @Valid @RequestBody OnboardingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Organization onboarded successfully",
                        tenantOnboardingService.onboard(request)));
    }
}