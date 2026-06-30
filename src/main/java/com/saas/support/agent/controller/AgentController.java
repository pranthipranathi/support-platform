package com.saas.support.agent.controller;

import com.saas.support.agent.dto.AgentResponse;
import com.saas.support.agent.dto.CreateAgentRequest;
import com.saas.support.agent.service.AgentService;
import com.saas.support.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/agents")
@RequiredArgsConstructor
@Tag(name = "Agents", description = "Support Agent Management APIs")
public class AgentController {

    private final AgentService agentService;

    @GetMapping
    @Operation(summary = "Get all agents")
    public ResponseEntity<ApiResponse<List<AgentResponse>>> getAllAgents() {
        return ResponseEntity.ok(
                ApiResponse.success(agentService.getAllAgents()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get agent by ID")
    public ResponseEntity<ApiResponse<AgentResponse>> getAgentById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(
                ApiResponse.success(agentService.getAgentById(id)));
    }

    @PostMapping
    @Operation(summary = "Create a new agent")
    public ResponseEntity<ApiResponse<AgentResponse>> createAgent(
            @Valid @RequestBody CreateAgentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Agent created successfully",
                        agentService.createAgent(request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an agent")
    public ResponseEntity<ApiResponse<Void>> deleteAgent(@PathVariable UUID id) {
        agentService.deleteAgent(id);
        return ResponseEntity.ok(
                ApiResponse.success("Agent deleted successfully", null));
    }
}