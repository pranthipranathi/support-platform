package com.saas.support.ai.provider;

import com.saas.support.ai.dto.AiRequest;
import com.saas.support.ai.dto.AiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "ai.provider", havingValue = "mock", matchIfMissing = true)
public class MockAiProvider implements AiProvider {

    @Override
    public AiResponse analyze(AiRequest request) {
        log.info("Mock AI analyzing ticket: {}", request.getSubject());
        return AiResponse.builder()
                .summary("This ticket is about: " + request.getSubject() +
                        ". The customer is experiencing an issue that requires attention.")
                .suggestedReply("Thank you for reaching out to our support team. " +
                        "We have received your request and will look into it shortly. " +
                        "Our team will get back to you within 24 hours.")
                .sentiment("NEUTRAL")
                .suggestedPriority("MEDIUM")
                .category("General Support")
                .build();
    }

    @Override
    public String generateReply(String ticketDescription, String customerMessage) {
        log.info("Mock AI generating reply");
        return "Thank you for contacting us. We understand your concern regarding: " +
                ticketDescription.substring(0, Math.min(50, ticketDescription.length())) +
                "... Our team is working on this and will provide a solution shortly.";
    }

    @Override
    public String summarize(String ticketDescription) {
        log.info("Mock AI summarizing ticket");
        int maxLength = Math.min(100, ticketDescription.length());
        return "Summary: " + ticketDescription.substring(0, maxLength) +
                (ticketDescription.length() > 100 ? "..." : "");
    }
}