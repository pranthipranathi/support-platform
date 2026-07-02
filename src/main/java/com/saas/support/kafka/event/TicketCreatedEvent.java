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
public class TicketCreatedEvent {

    private UUID ticketId;
    private String subject;
    private String priority;
    private String status;
    private UUID customerId;
    private UUID assignedTo;
    private LocalDateTime createdAt;
}