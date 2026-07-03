package com.saas.support.ai.provider;

import com.saas.support.ai.dto.AiRequest;
import com.saas.support.ai.dto.AiResponse;

public interface AiProvider {
    AiResponse analyze(AiRequest request);
    String generateReply(String ticketDescription, String customerMessage);
    String summarize(String ticketDescription);
}