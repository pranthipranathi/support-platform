package com.saas.support.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketUpdatedEvent {

    private UUID ticketId;
    private String subject;
    private String oldStatus;
    private String newStatus;
    private String priority;
    private UUID assignedTo;
    private LocalDateTime updatedAt;
}