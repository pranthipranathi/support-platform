package com.saas.support.ticket.repository;

import com.saas.support.common.enums.TicketPriority;
import com.saas.support.common.enums.TicketStatus;
import com.saas.support.ticket.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    Page<Ticket> findAll(Pageable pageable);

    List<Ticket> findByCustomerId(UUID customerId);

    List<Ticket> findByAssignedTo(UUID agentId);

    List<Ticket> findByStatus(TicketStatus status);

    List<Ticket> findByPriority(TicketPriority priority);

    long countByStatus(TicketStatus status);

    long countByAssignedTo(UUID agentId);

    long countByPriority(TicketPriority priority);

    long countBySlaBreached(boolean slaBreached);
}