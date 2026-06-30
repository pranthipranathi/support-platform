package com.saas.support.agent.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AgentResponse {

    private UUID id;
    private UUID userId;
    private String department;
    private Integer maxTickets;
    private boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}