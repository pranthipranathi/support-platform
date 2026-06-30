package com.saas.support.comment.controller;

import com.saas.support.comment.dto.CommentResponse;
import com.saas.support.comment.dto.CreateCommentRequest;
import com.saas.support.comment.service.CommentService;
import com.saas.support.common.response.ApiResponse;
import com.saas.support.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Ticket Comments APIs")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/ticket/{ticketId}")
    @Operation(summary = "Get comments for a ticket")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByTicket(
            @PathVariable UUID ticketId) {
        return ResponseEntity.ok(
                ApiResponse.success(commentService.getCommentsByTicket(ticketId)));
    }

    @PostMapping
    @Operation(summary = "Add a comment to a ticket")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @Valid @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Comment added successfully",
                        commentService.createComment(request, currentUser.getId())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a comment")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable UUID id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok(
                ApiResponse.success("Comment deleted successfully", null));
    }
}