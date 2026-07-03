package com.saas.support.admin.service;

import com.saas.support.admin.dto.AdminStatsResponse;
import com.saas.support.agent.repository.AgentRepository;
import com.saas.support.audit.repository.AuditLogRepository;
import com.saas.support.customer.repository.CustomerRepository;
import com.saas.support.knowledge.repository.KbArticleRepository;
import com.saas.support.notification.repository.NotificationRepository;
import com.saas.support.permission.repository.PermissionRepository;
import com.saas.support.role.repository.RoleRepository;
import com.saas.support.tenant.repository.TenantRepository;
import com.saas.support.ticket.repository.TicketRepository;
import com.saas.support.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final AgentRepository agentRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final KbArticleRepository kbArticleRepository;
    private final AuditLogRepository auditLogRepository;
    private final NotificationRepository notificationRepository;
    private final TenantRepository tenantRepository;

    public AdminStatsResponse getSystemStats() {
        return AdminStatsResponse.builder()
                .totalOrganizations(tenantRepository.count())
                .totalUsers(userRepository.count())
                .totalTickets(ticketRepository.count())
                .totalCustomers(customerRepository.count())
                .totalAgents(agentRepository.count())
                .totalRoles(roleRepository.count())
                .totalPermissions(permissionRepository.count())
                .totalKbArticles(kbArticleRepository.count())
                .totalAuditLogs(auditLogRepository.count())
                .totalNotifications(notificationRepository.count())
                .build();
    }
}