package com.saas.support.ai.service;

import com.saas.support.ai.dto.AiRequest;
import com.saas.support.ai.dto.AiResponse;
import com.saas.support.ai.provider.AiProvider;
import com.saas.support.common.exception.ResourceNotFoundException;
import com.saas.support.ticket.entity.Ticket;
import com.saas.support.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final AiProvider aiProvider;
    private final TicketRepository ticketRepository;

    public AiResponse analyzeTicket(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", ticketId.toString()));

        AiRequest request = new AiRequest();
        request.setTicketId(ticketId.toString());
        request.setSubject(ticket.getSubject());
        request.setDescription(ticket.getDescription());

        return aiProvider.analyze(request);
    }

    public String generateReply(UUID ticketId, String customerMessage) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", ticketId.toString()));

        return aiProvider.generateReply(ticket.getDescription(), customerMessage);
    }

    @Transactional
    public String summarizeTicket(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", ticketId.toString()));

        String summary = aiProvider.summarize(ticket.getDescription());
        ticket.setAiSummary(summary);
        ticketRepository.save(ticket);
        log.info("AI summary saved for ticket: {}", ticketId);
        return summary;
    }
}