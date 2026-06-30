package com.saas.support.tenant.service;

import com.saas.support.common.exception.TenantNotFoundException;
import com.saas.support.tenant.entity.Tenant;
import com.saas.support.tenant.repository.TenantRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Tenant findBySchemaName(String schemaName) {
        return tenantRepository.findBySchemaName(schemaName)
                .orElseThrow(() -> new TenantNotFoundException(
                        "Tenant not found: " + schemaName));
    }

    public boolean tenantExists(String schemaName) {
        return tenantRepository.existsBySchemaName(schemaName);
    }

    @Transactional
    public void createTenantSchema(String schemaName) {
        log.info("Creating schema for tenant: {}", schemaName);
        entityManager.createNativeQuery(
                        "SELECT public.create_tenant_schema(:schema)")
                .setParameter("schema", schemaName)
                .getSingleResult();
        log.info("Schema created successfully for tenant: {}", schemaName);
    }

    public Tenant findById(UUID id) {
        return tenantRepository.findById(id)
                .orElseThrow(() -> new TenantNotFoundException(
                        "Tenant not found with id: " + id));
    }
}