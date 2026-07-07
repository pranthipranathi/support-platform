package com.saas.support.ticket.service;

import com.saas.support.common.enums.TicketStatus;
import com.saas.support.common.exception.ResourceNotFoundException;
import com.saas.support.kafka.event.TicketCreatedEvent;
import com.saas.support.kafka.event.TicketUpdatedEvent;
import com.saas.support.kafka.producer.TicketEventProducer;
import com.saas.support.ticket.dto.CreateTicketRequest;
import com.saas.support.ticket.dto.TicketResponse;
import com.saas.support.ticket.dto.UpdateTicketRequest;
import com.saas.support.ticket.entity.Ticket;
import com.saas.support.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketEventProducer ticketEventProducer;

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TicketResponse getTicketById(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", id.toString()));
        return mapToResponse(ticket);
    }

    public List<TicketResponse> getTicketsByCustomer(UUID customerId) {
        return ticketRepository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TicketResponse> getTicketsByAgent(UUID agentId) {
        return ticketRepository.findByAssignedTo(agentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TicketResponse createTicket(CreateTicketRequest request) {
        Ticket ticket = Ticket.builder()
                .subject(request.getSubject())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(TicketStatus.OPEN)
                .customerId(request.getCustomerId())
                .assignedTo(request.getAssignedTo())
                .teamId(request.getTeamId())
                .category(request.getCategory())
                .source(request.getSource() != null ? request.getSource() : "WEB")
                .slaBreached(false)
                .build();

        Ticket saved = ticketRepository.save(ticket);
        log.info("Ticket created: {}", saved.getId());

        ticketEventProducer.publishTicketCreated(TicketCreatedEvent.builder()
                .ticketId(saved.getId())
                .subject(saved.getSubject())
                .priority(saved.getPriority().name())
                .status(saved.getStatus().name())
                .customerId(saved.getCustomerId())
                .assignedTo(saved.getAssignedTo())
                .createdAt(saved.getCreatedAt())
                .build());

        return mapToResponse(saved);
    }

    @Transactional
    public TicketResponse updateTicket(UUID id, UpdateTicketRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", id.toString()));

        String oldStatus = ticket.getStatus().name();

        if (request.getSubject() != null) ticket.setSubject(request.getSubject());
        if (request.getDescription() != null) ticket.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            ticket.setStatus(request.getStatus());
            if (request.getStatus() == TicketStatus.RESOLVED) {
                ticket.setResolvedAt(LocalDateTime.now());
            }
            if (request.getStatus() == TicketStatus.CLOSED) {
                ticket.setClosedAt(LocalDateTime.now());
            }
        }
        if (request.getPriority() != null) ticket.setPriority(request.getPriority());
        if (request.getCategory() != null) ticket.setCategory(request.getCategory());
        if (request.getAssignedTo() != null) ticket.setAssignedTo(request.getAssignedTo());
        if (request.getTeamId() != null) ticket.setTeamId(request.getTeamId());

        Ticket updated = ticketRepository.save(ticket);
        log.info("Ticket updated: {}", updated.getId());

        ticketEventProducer.publishTicketUpdated(TicketUpdatedEvent.builder()
                .ticketId(updated.getId())
                .subject(updated.getSubject())
                .oldStatus(oldStatus)
                .newStatus(updated.getStatus().name())
                .priority(updated.getPriority().name())
                .assignedTo(updated.getAssignedTo())
                .updatedAt(updated.getUpdatedAt())
                .build());

        return mapToResponse(updated);
    }

    @Transactional
    public void deleteTicket(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", id.toString()));
        ticketRepository.delete(ticket);
        log.info("Ticket deleted: {}", id);
    }

    private TicketResponse mapToResponse(Ticket ticket) {
        TicketResponse response = new TicketResponse();
        response.setId(ticket.getId());
        response.setSubject(ticket.getSubject());
        response.setDescription(ticket.getDescription());
        response.setStatus(ticket.getStatus());
        response.setPriority(ticket.getPriority());
        response.setCategory(ticket.getCategory());
        response.setCustomerId(ticket.getCustomerId());
        response.setAssignedTo(ticket.getAssignedTo());
        response.setTeamId(ticket.getTeamId());
        response.setSource(ticket.getSource());
        response.setAiSummary(ticket.getAiSummary());
        response.setSlaBreached(ticket.isSlaBreached());
        response.setDueDate(ticket.getDueDate());
        response.setResolvedAt(ticket.getResolvedAt());
        response.setClosedAt(ticket.getClosedAt());
        response.setCreatedAt(ticket.getCreatedAt() != null ? ticket.getCreatedAt() : java.time.LocalDateTime.now());
        response.setUpdatedAt(ticket.getUpdatedAt() != null ? ticket.getUpdatedAt() : java.time.LocalDateTime.now());
        return response;
    }
}