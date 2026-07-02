package com.saas.support.kafka.consumer;

import com.saas.support.kafka.event.TicketCreatedEvent;
import com.saas.support.kafka.event.TicketUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TicketEventConsumer {

    @KafkaListener(topics = "ticket.created", groupId = "support-platform-group")
    public void handleTicketCreated(TicketCreatedEvent event) {
        log.info("Received ticket created event: {} - {}",
                event.getTicketId(), event.getSubject());
    }

    @KafkaListener(topics = "ticket.updated", groupId = "support-platform-group")
    public void handleTicketUpdated(TicketUpdatedEvent event) {
        log.info("Received ticket updated event: {} - status changed from {} to {}",
                event.getTicketId(), event.getOldStatus(), event.getNewStatus());
    }
}