package com.saas.support.role.service;

import com.saas.support.common.exception.BusinessException;
import com.saas.support.common.exception.ResourceNotFoundException;
import com.saas.support.role.dto.CreateRoleRequest;
import com.saas.support.role.dto.RoleResponse;
import com.saas.support.role.entity.Role;
import com.saas.support.role.repository.RoleRepository;
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
public class RoleService {

    private final RoleRepository roleRepository;

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RoleResponse getRoleById(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id.toString()));
        return mapToResponse(role);
    }

    @Transactional
    public RoleResponse createRole(CreateRoleRequest request) {
        if (roleRepository.existsByName(request.getName())) {
            throw new BusinessException("Role already exists: " + request.getName());
        }

        Role role = Role.builder()
                .name(request.getName().toUpperCase())
                .displayName(request.getDisplayName())
                .description(request.getDescription())
                .isSystem(false)
                .isActive(true)
                .build();

        Role saved = roleRepository.save(role);
        log.info("Role created: {}", saved.getName());
        return mapToResponse(saved);
    }

    private RoleResponse mapToResponse(Role role) {
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setName(role.getName());
        response.setDisplayName(role.getDisplayName());
        response.setDescription(role.getDescription());
        response.setSystem(role.isSystem());
        response.setActive(role.isActive());
        response.setCreatedAt(role.getCreatedAt());
        return response;
    }
}