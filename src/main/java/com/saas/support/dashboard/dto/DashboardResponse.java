package com.saas.support.dashboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {

    private long totalTickets;
    private long openTickets;
    private long inProgressTickets;
    private long resolvedTickets;
    private long closedTickets;
    private long totalCustomers;
    private long totalAgents;
    private long totalUsers;
    private long unreadNotifications;
}