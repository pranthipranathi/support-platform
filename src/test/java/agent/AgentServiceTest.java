package com.saas.support.agent;

import com.saas.support.agent.dto.AgentResponse;
import com.saas.support.agent.dto.CreateAgentRequest;
import com.saas.support.agent.entity.Agent;
import com.saas.support.agent.repository.AgentRepository;
import com.saas.support.agent.service.AgentService;
import com.saas.support.common.exception.BusinessException;
import com.saas.support.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgentServiceTest {

    @Mock
    private AgentRepository agentRepository;

    @InjectMocks
    private AgentService agentService;

    private Agent agent;
    private UUID agentId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        agentId = UUID.randomUUID();
        userId = UUID.randomUUID();

        agent = Agent.builder()
                .id(agentId)
                .userId(userId)
                .department("Technical Support")
                .maxTickets(20)
                .isAvailable(true)
                .build();
    }

    @Test
    void getAllAgents_returnsAllAgents() {
        when(agentRepository.findAll()).thenReturn(List.of(agent));

        List<AgentResponse> result = agentService.getAllAgents();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDepartment()).isEqualTo("Technical Support");
    }

    @Test
    void getAgentById_existingId_returnsAgent() {
        when(agentRepository.findById(agentId)).thenReturn(Optional.of(agent));

        AgentResponse result = agentService.getAgentById(agentId);

        assertThat(result.getId()).isEqualTo(agentId);
        assertThat(result.getMaxTickets()).isEqualTo(20);
    }

    @Test
    void getAgentById_nonExistingId_throwsException() {
        UUID nonExistingId = UUID.randomUUID();
        when(agentRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> agentService.getAgentById(nonExistingId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createAgent_newUser_returnsCreatedAgent() {
        CreateAgentRequest request = new CreateAgentRequest();
        request.setUserId(userId);
        request.setDepartment("Technical Support");
        request.setMaxTickets(25);

        when(agentRepository.existsByUserId(userId)).thenReturn(false);
        when(agentRepository.save(any(Agent.class))).thenReturn(agent);

        AgentResponse result = agentService.createAgent(request);

        assertThat(result).isNotNull();
        verify(agentRepository).save(any(Agent.class));
    }

    @Test
    void createAgent_existingUser_throwsException() {
        CreateAgentRequest request = new CreateAgentRequest();
        request.setUserId(userId);

        when(agentRepository.existsByUserId(userId)).thenReturn(true);

        assertThatThrownBy(() -> agentService.createAgent(request))
                .isInstanceOf(BusinessException.class);
    }
}