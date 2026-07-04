package com.saas.support.ticket;

import com.saas.support.common.enums.TicketPriority;
import com.saas.support.common.enums.TicketStatus;
import com.saas.support.common.exception.ResourceNotFoundException;
import com.saas.support.kafka.producer.TicketEventProducer;
import com.saas.support.ticket.dto.CreateTicketRequest;
import com.saas.support.ticket.dto.TicketResponse;
import com.saas.support.ticket.entity.Ticket;
import com.saas.support.ticket.repository.TicketRepository;
import com.saas.support.ticket.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketEventProducer ticketEventProducer;

    @InjectMocks
    private TicketService ticketService;

    private Ticket ticket;
    private UUID ticketId;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        ticketId = UUID.randomUUID();
        customerId = UUID.randomUUID();

        ticket = Ticket.builder()
                .id(ticketId)
                .subject("Test ticket")
                .description("Test description")
                .status(TicketStatus.OPEN)
                .priority(TicketPriority.MEDIUM)
                .customerId(customerId)
                .source("WEB")
                .slaBreached(false)
                .build();
    }

    @Test
    void getAllTickets_returnsAllTickets() {
        when(ticketRepository.findAll()).thenReturn(List.of(ticket));

        List<TicketResponse> result = ticketService.getAllTickets();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSubject()).isEqualTo("Test ticket");
        verify(ticketRepository).findAll();
    }

    @Test
    void getTicketById_existingId_returnsTicket() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        TicketResponse result = ticketService.getTicketById(ticketId);

        assertThat(result.getId()).isEqualTo(ticketId);
        assertThat(result.getStatus()).isEqualTo(TicketStatus.OPEN);
    }

    @Test
    void getTicketById_nonExistingId_throwsException() {
        UUID nonExistingId = UUID.randomUUID();
        when(ticketRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.getTicketById(nonExistingId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createTicket_validRequest_returnsCreatedTicket() {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setSubject("New ticket");
        request.setDescription("New description");
        request.setPriority(TicketPriority.HIGH);
        request.setCustomerId(customerId);

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        doNothing().when(ticketEventProducer).publishTicketCreated(any());

        TicketResponse result = ticketService.createTicket(request);

        assertThat(result).isNotNull();
        verify(ticketRepository).save(any(Ticket.class));
        verify(ticketEventProducer).publishTicketCreated(any());
    }

    @Test
    void deleteTicket_existingId_deletesTicket() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        doNothing().when(ticketRepository).delete(ticket);

        ticketService.deleteTicket(ticketId);

        verify(ticketRepository).delete(ticket);
    }
}