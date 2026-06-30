package com.saas.support.tenant.repository;

import com.saas.support.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, UUID> {

    Optional<Tenant> findBySchemaName(String schemaName);

    boolean existsBySchemaName(String schemaName);
}