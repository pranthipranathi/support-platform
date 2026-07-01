package com.saas.support.report.controller;

import com.saas.support.common.response.ApiResponse;
import com.saas.support.report.dto.TicketReportResponse;
import com.saas.support.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Reporting APIs")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/tickets")
    @Operation(summary = "Get ticket report")
    public ResponseEntity<ApiResponse<TicketReportResponse>> getTicketReport() {
        return ResponseEntity.ok(
                ApiResponse.success(reportService.getTicketReport()));
    }
}