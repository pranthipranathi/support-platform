package com.saas.support.comment.service;

import com.saas.support.comment.dto.CommentResponse;
import com.saas.support.comment.dto.CreateCommentRequest;
import com.saas.support.comment.entity.Comment;
import com.saas.support.comment.repository.CommentRepository;
import com.saas.support.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<CommentResponse> getCommentsByTicket(UUID ticketId) {
        return commentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse createComment(CreateCommentRequest request, UUID authorId) {
        Comment comment = Comment.builder()
                .ticketId(request.getTicketId())
                .authorId(authorId)
                .content(request.getContent())
                .isInternal(request.isInternal())
                .build();

        Comment saved = commentRepository.save(comment);
        log.info("Comment created on ticket: {}", saved.getTicketId());
        return mapToResponse(saved);
    }

    @Transactional
    public void deleteComment(UUID id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", id.toString()));
        commentRepository.delete(comment);
        log.info("Comment deleted: {}", id);
    }

    private CommentResponse mapToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setTicketId(comment.getTicketId());
        response.setAuthorId(comment.getAuthorId());
        response.setContent(comment.getContent());
        response.setInternal(comment.isInternal());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());
        return response;
    }
}