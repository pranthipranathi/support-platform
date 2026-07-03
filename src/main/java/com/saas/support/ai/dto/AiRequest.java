package com.saas.support.ai.dto;

import lombok.Data;

@Data
public class AiRequest {
    private String ticketId;
    private String subject;
    private String description;
    private String customerMessage;
}