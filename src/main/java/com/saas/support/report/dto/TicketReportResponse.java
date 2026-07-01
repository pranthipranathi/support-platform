package com.saas.support.report.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketReportResponse {

    private long totalTickets;
    private long openTickets;
    private long inProgressTickets;
    private long resolvedTickets;
    private long closedTickets;
    private long highPriorityTickets;
    private long urgentTickets;
    private long criticalTickets;
    private long slaBreachedTickets;
}