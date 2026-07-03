package com.saas.support.ai.controller;

import com.saas.support.ai.dto.AiResponse;
import com.saas.support.ai.service.AiService;
import com.saas.support.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@Tag(name = "AI Assistant", description = "AI-powered ticket analysis APIs")
public class AiController {

    private final AiService aiService;

    @GetMapping("/tickets/{ticketId}/analyze")
    @Operation(summary = "Analyze a ticket with AI")
    public ResponseEntity<ApiResponse<AiResponse>> analyzeTicket(
            @PathVariable UUID ticketId) {
        return ResponseEntity.ok(
                ApiResponse.success(aiService.analyzeTicket(ticketId)));
    }

    @PostMapping("/tickets/{ticketId}/reply")
    @Operation(summary = "Generate a reply suggestion for a ticket")
    public ResponseEntity<ApiResponse<String>> generateReply(
            @PathVariable UUID ticketId,
            @RequestParam String customerMessage) {
        return ResponseEntity.ok(
                ApiResponse.success(aiService.generateReply(ticketId, customerMessage)));
    }

    @PostMapping("/tickets/{ticketId}/summarize")
    @Operation(summary = "Summarize a ticket and save the summary")
    public ResponseEntity<ApiResponse<String>> summarizeTicket(
            @PathVariable UUID ticketId) {
        return ResponseEntity.ok(
                ApiResponse.success(aiService.summarizeTicket(ticketId)));
    }
}