package com.saas.support.ticket.dto;

import com.saas.support.common.enums.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateTicketRequest {

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Priority is required")
    private TicketPriority priority;

    @NotNull(message = "Customer ID is required")
    private UUID customerId;

    private String category;
    private String source;
    private UUID assignedTo;
    private UUID teamId;
}