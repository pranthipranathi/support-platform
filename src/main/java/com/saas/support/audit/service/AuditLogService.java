package com.saas.support.audit.service;

import com.saas.support.audit.dto.AuditLogResponse;
import com.saas.support.audit.entity.AuditLog;
import com.saas.support.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public List<AuditLogResponse> getLogsByUser(UUID userId) {
        return auditLogRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<AuditLogResponse> getLogsByResource(String resource, UUID resourceId) {
        return auditLogRepository.findByResourceAndResourceIdOrderByCreatedAtDesc(resource, resourceId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public void log(UUID userId, String action, String resource, UUID resourceId,
                    String oldValues, String newValues, String ipAddress) {
        AuditLog auditLog = AuditLog.builder()
                .userId(userId)
                .action(action)
                .resource(resource)
                .resourceId(resourceId)
                .oldValues(oldValues)
                .newValues(newValues)
                .ipAddress(ipAddress)
                .build();
        auditLogRepository.save(auditLog);
        log.info("Audit log: {} {} {}", action, resource, resourceId);
    }

    private AuditLogResponse mapToResponse(AuditLog log) {
        AuditLogResponse response = new AuditLogResponse();
        response.setId(log.getId());
        response.setUserId(log.getUserId());
        response.setAction(log.getAction());
        response.setResource(log.getResource());
        response.setResourceId(log.getResourceId());
        response.setOldValues(log.getOldValues());
        response.setNewValues(log.getNewValues());
        response.setIpAddress(log.getIpAddress());
        response.setCreatedAt(log.getCreatedAt());
        return response;
    }
}