package com.saas.support.tenant.entity;

import com.saas.support.common.enums.TenantStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenants", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "schema_name", nullable = false, unique = true)
    private String schemaName;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TenantStatus status;

    @Column(nullable = false)
    private String plan;

    @Column(name = "max_users")
    private Integer maxUsers;

    @Column(name = "max_agents")
    private Integer maxAgents;

    @Column(name = "max_tickets_pm")
    private Integer maxTicketsPm;

    @Column(name = "trial_ends_at")
    private LocalDateTime trialEndsAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}