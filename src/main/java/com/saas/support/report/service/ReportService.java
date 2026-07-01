package com.saas.support.report.service;

import com.saas.support.common.enums.TicketPriority;
import com.saas.support.common.enums.TicketStatus;
import com.saas.support.report.dto.TicketReportResponse;
import com.saas.support.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final TicketRepository ticketRepository;

    public TicketReportResponse getTicketReport() {
        return TicketReportResponse.builder()
                .totalTickets(ticketRepository.count())
                .openTickets(ticketRepository.countByStatus(TicketStatus.OPEN))
                .inProgressTickets(ticketRepository.countByStatus(TicketStatus.IN_PROGRESS))
                .resolvedTickets(ticketRepository.countByStatus(TicketStatus.RESOLVED))
                .closedTickets(ticketRepository.countByStatus(TicketStatus.CLOSED))
                .highPriorityTickets(ticketRepository.countByPriority(TicketPriority.HIGH))
                .urgentTickets(ticketRepository.countByPriority(TicketPriority.URGENT))
                .criticalTickets(ticketRepository.countByPriority(TicketPriority.CRITICAL))
                .slaBreachedTickets(ticketRepository.countBySlaBreached(true))
                .build();
    }
}