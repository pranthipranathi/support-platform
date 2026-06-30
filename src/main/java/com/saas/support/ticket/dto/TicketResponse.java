package com.saas.support.ticket.dto;

import com.saas.support.common.enums.TicketPriority;
import com.saas.support.common.enums.TicketStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TicketResponse {

    private UUID id;
    private String subject;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private String category;
    private UUID customerId;
    private UUID assignedTo;
    private UUID teamId;
    private String source;
    private String aiSummary;
    private boolean slaBreached;
    private LocalDateTime dueDate;
    private LocalDateTime resolvedAt;
    private LocalDateTime closedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}