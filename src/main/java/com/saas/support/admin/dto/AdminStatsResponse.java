package com.saas.support.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminStatsResponse {

    private long totalOrganizations;
    private long totalUsers;
    private long totalTickets;
    private long totalCustomers;
    private long totalAgents;
    private long totalRoles;
    private long totalPermissions;
    private long totalKbArticles;
    private long totalAuditLogs;
    private long totalNotifications;
}