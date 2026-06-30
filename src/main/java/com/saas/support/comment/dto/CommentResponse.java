package com.saas.support.comment.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CommentResponse {

    private UUID id;
    private UUID ticketId;
    private UUID authorId;
    private String content;
    private boolean isInternal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
