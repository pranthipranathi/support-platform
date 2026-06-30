package com.saas.support.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateCommentRequest {

    @NotNull(message = "Ticket ID is required")
    private UUID ticketId;

    @NotBlank(message = "Content is required")
    private String content;

    private boolean isInternal;
}