package com.saas.support.kafka.producer;

import com.saas.support.kafka.event.TicketCreatedEvent;
import com.saas.support.kafka.event.TicketUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketEventProducer {

    private static final String TICKET_CREATED_TOPIC = "ticket.created";
    private static final String TICKET_UPDATED_TOPIC = "ticket.updated";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishTicketCreated(TicketCreatedEvent event) {
        log.info("Publishing ticket created event: {}", event.getTicketId());
        kafkaTemplate.send(TICKET_CREATED_TOPIC, event.getTicketId().toString(), event);
    }

    public void publishTicketUpdated(TicketUpdatedEvent event) {
        log.info("Publishing ticket updated event: {}", event.getTicketId());
        kafkaTemplate.send(TICKET_UPDATED_TOPIC, event.getTicketId().toString(), event);
    }
}