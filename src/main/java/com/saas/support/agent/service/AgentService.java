package com.saas.support.agent.service;

import com.saas.support.agent.dto.AgentResponse;
import com.saas.support.agent.dto.CreateAgentRequest;
import com.saas.support.agent.entity.Agent;
import com.saas.support.agent.repository.AgentRepository;
import com.saas.support.common.exception.BusinessException;
import com.saas.support.common.exception.ResourceNotFoundException;
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
public class AgentService {

    private final AgentRepository agentRepository;

    public List<AgentResponse> getAllAgents() {
        return agentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public AgentResponse getAgentById(UUID id) {
        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agent", id.toString()));
        return mapToResponse(agent);
    }

    @Transactional
    public AgentResponse createAgent(CreateAgentRequest request) {
        if (agentRepository.existsByUserId(request.getUserId())) {
            throw new BusinessException("This user is already an agent");
        }

        Agent agent = Agent.builder()
                .userId(request.getUserId())
                .department(request.getDepartment())
                .maxTickets(request.getMaxTickets() != null ? request.getMaxTickets() : 20)
                .isAvailable(true)
                .build();

        Agent saved = agentRepository.save(agent);
        log.info("Agent created: {}", saved.getId());
        return mapToResponse(saved);
    }

    @Transactional
    public void deleteAgent(UUID id) {
        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agent", id.toString()));
        agentRepository.delete(agent);
        log.info("Agent deleted: {}", id);
    }

    private AgentResponse mapToResponse(Agent agent) {
        AgentResponse response = new AgentResponse();
        response.setId(agent.getId());
        response.setUserId(agent.getUserId());
        response.setDepartment(agent.getDepartment());
        response.setMaxTickets(agent.getMaxTickets());
        response.setAvailable(agent.isAvailable());
        response.setCreatedAt(agent.getCreatedAt());
        response.setUpdatedAt(agent.getUpdatedAt());
        return response;
    }
}