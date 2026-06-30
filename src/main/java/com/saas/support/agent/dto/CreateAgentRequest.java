package com.saas.support.agent.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateAgentRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    private String department;
    private Integer maxTickets;
}