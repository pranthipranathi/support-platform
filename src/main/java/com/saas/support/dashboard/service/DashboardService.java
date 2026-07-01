package com.saas.support.dashboard.service;

import com.saas.support.agent.repository.AgentRepository;
import com.saas.support.common.enums.TicketStatus;
import com.saas.support.customer.repository.CustomerRepository;
import com.saas.support.dashboard.dto.DashboardResponse;
import com.saas.support.notification.repository.NotificationRepository;
import com.saas.support.ticket.repository.TicketRepository;
import com.saas.support.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final AgentRepository agentRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public DashboardResponse getDashboardStats(UUID currentUserId) {
        return DashboardResponse.builder()
                .totalTickets(ticketRepository.count())
                .openTickets(ticketRepository.countByStatus(TicketStatus.OPEN))
                .inProgressTickets(ticketRepository.countByStatus(TicketStatus.IN_PROGRESS))
                .resolvedTickets(ticketRepository.countByStatus(TicketStatus.RESOLVED))
                .closedTickets(ticketRepository.countByStatus(TicketStatus.CLOSED))
                .totalCustomers(customerRepository.count())
                .totalAgents(agentRepository.count())
                .totalUsers(userRepository.count())
                .unreadNotifications(notificationRepository.countByUserIdAndIsReadFalse(currentUserId))
                .build();
    }
}