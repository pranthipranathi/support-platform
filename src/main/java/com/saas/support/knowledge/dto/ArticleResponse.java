package com.saas.support.knowledge.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ArticleResponse {

    private UUID id;
    private String title;
    private String content;
    private String excerpt;
    private String status;
    private UUID authorId;
    private Integer views;
    private Integer helpfulVotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}