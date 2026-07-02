package com.saas.support.knowledge.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "kb_articles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KbArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String excerpt;

    @Column(nullable = false)
    private String status;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    private Integer views;

    @Column(name = "helpful_votes")
    private Integer helpfulVotes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}