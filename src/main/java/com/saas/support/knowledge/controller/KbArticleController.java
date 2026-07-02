package com.saas.support.knowledge.controller;

import com.saas.support.common.response.ApiResponse;
import com.saas.support.knowledge.dto.ArticleResponse;
import com.saas.support.knowledge.dto.CreateArticleRequest;
import com.saas.support.knowledge.service.KbArticleService;
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
@RequestMapping("/api/v1/knowledge")
@RequiredArgsConstructor
@Tag(name = "Knowledge Base", description = "Knowledge Base APIs")
public class KbArticleController {

    private final KbArticleService articleService;

    @GetMapping
    @Operation(summary = "Get all published articles")
    public ResponseEntity<ApiResponse<List<ArticleResponse>>> getAllPublished() {
        return ResponseEntity.ok(ApiResponse.success(articleService.getAllPublished()));
    }

    @GetMapping("/search")
    @Operation(summary = "Search articles by keyword")
    public ResponseEntity<ApiResponse<List<ArticleResponse>>> search(
            @RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(articleService.search(keyword)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get article by ID")
    public ResponseEntity<ApiResponse<ArticleResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(articleService.getById(id)));
    }

    @PostMapping
    @Operation(summary = "Create a new article")
    public ResponseEntity<ApiResponse<ArticleResponse>> createArticle(
            @Valid @RequestBody CreateArticleRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Article created",
                        articleService.createArticle(request, currentUser)));
    }

    @PatchMapping("/{id}/publish")
    @Operation(summary = "Publish an article")
    public ResponseEntity<ApiResponse<ArticleResponse>> publish(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Article published",
                articleService.publish(id)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an article")
    public ResponseEntity<ApiResponse<Void>> deleteArticle(@PathVariable UUID id) {
        articleService.deleteArticle(id);
        return ResponseEntity.ok(ApiResponse.success("Article deleted", null));
    }
}