package com.saas.support.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiResponse {
    private String summary;
    private String suggestedReply;
    private String sentiment;
    private String suggestedPriority;
    private String category;
}