package com.saas.support.ticket.dto;

import com.saas.support.common.enums.TicketPriority;
import com.saas.support.common.enums.TicketStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateTicketRequest {

    private String subject;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private String category;
    private UUID assignedTo;
    private UUID teamId;
}