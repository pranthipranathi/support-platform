package com.saas.support.tenant.service;

import com.saas.support.common.enums.TenantStatus;
import com.saas.support.common.exception.BusinessException;
import com.saas.support.tenant.dto.OnboardingRequest;
import com.saas.support.tenant.dto.OnboardingResponse;
import com.saas.support.tenant.entity.Organization;
import com.saas.support.tenant.entity.Tenant;
import com.saas.support.tenant.repository.OrganizationRepository;
import com.saas.support.tenant.repository.TenantRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantOnboardingService {

    private final OrganizationRepository organizationRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public OnboardingResponse onboard(OnboardingRequest request) {
        if (organizationRepository.existsBySlug(request.getSlug())) {
            throw new BusinessException("Slug already taken: " + request.getSlug());
        }
        if (organizationRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Organization email already registered");
        }

        // Create organization
        Organization organization = Organization.builder()
                .name(request.getOrganizationName())
                .slug(request.getSlug())
                .email(request.getEmail())
                .phone(request.getPhone())
                .isActive(true)
                .build();
        organization = organizationRepository.save(organization);
        log.info("Organization created: {}", organization.getId());

        // Create tenant
        String schemaName = "tenant_" + request.getSlug().replace("-", "_");
        String plan = request.getPlan() != null ? request.getPlan() : "STARTER";

        Tenant tenant = Tenant.builder()
                .organizationId(organization.getId())
                .schemaName(schemaName)
                .status(TenantStatus.ACTIVE)
                .plan(plan)
                .maxUsers(10)
                .maxAgents(5)
                .maxTicketsPm(1000)
                .build();
        tenant = tenantRepository.save(tenant);
        log.info("Tenant created: {}", tenant.getId());

        // Create tenant schema
        entityManager.createNativeQuery(
                        "SELECT public.create_tenant_schema(:schema)")
                .setParameter("schema", schemaName)
                .getSingleResult();
        log.info("Schema created: {}", schemaName);

        // Create admin user in tenant schema
        UUID adminUserId = UUID.randomUUID();
        String hashedPassword = passwordEncoder.encode(request.getAdminPassword());

        entityManager.createNativeQuery(
                        "INSERT INTO " + schemaName + ".users " +
                                "(id, email, password_hash, first_name, last_name, is_active, is_email_verified) " +
                                "VALUES (:id, :email, :password, :firstName, :lastName, true, true)")
                .setParameter("id", adminUserId)
                .setParameter("email", request.getAdminEmail())
                .setParameter("password", "{bcrypt}" + hashedPassword)
                .setParameter("firstName", request.getAdminFirstName())
                .setParameter("lastName", request.getAdminLastName())
                .executeUpdate();
        log.info("Admin user created in schema: {}", schemaName);

        return OnboardingResponse.builder()
                .organizationId(organization.getId())
                .tenantId(tenant.getId())
                .adminUserId(adminUserId)
                .schemaName(schemaName)
                .organizationName(organization.getName())
                .slug(request.getSlug())
                .adminEmail(request.getAdminEmail())
                .plan(plan)
                .message("Organization onboarded successfully! Use X-Tenant-ID: " + request.getSlug() + " header to login.")
                .build();
    }
}