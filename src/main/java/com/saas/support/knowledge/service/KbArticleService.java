package com.saas.support.knowledge.service;

import com.saas.support.common.exception.ResourceNotFoundException;
import com.saas.support.knowledge.dto.ArticleResponse;
import com.saas.support.knowledge.dto.CreateArticleRequest;
import com.saas.support.knowledge.entity.KbArticle;
import com.saas.support.knowledge.repository.KbArticleRepository;
import com.saas.support.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KbArticleService {

    private final KbArticleRepository articleRepository;

    public List<ArticleResponse> getAllPublished() {
        return articleRepository.findByStatus("PUBLISHED")
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<ArticleResponse> getAllArticles() {
        return articleRepository.findAll()
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<ArticleResponse> search(String keyword) {
        return articleRepository.findByTitleContainingIgnoreCase(keyword)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public ArticleResponse getById(UUID id) {
        KbArticle article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", id.toString()));
        article.setViews(article.getViews() == null ? 1 : article.getViews() + 1);
        articleRepository.save(article);
        return mapToResponse(article);
    }

    @Transactional
    public ArticleResponse createArticle(CreateArticleRequest request, User author) {
        KbArticle article = KbArticle.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .excerpt(request.getExcerpt())
                .status("DRAFT")
                .authorId(author.getId())
                .views(0)
                .helpfulVotes(0)
                .build();

        KbArticle saved = articleRepository.save(article);
        log.info("KB article created: {}", saved.getId());
        return mapToResponse(saved);
    }

    @Transactional
    public ArticleResponse publish(UUID id) {
        KbArticle article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", id.toString()));
        article.setStatus("PUBLISHED");
        return mapToResponse(articleRepository.save(article));
    }

    @Transactional
    public void deleteArticle(UUID id) {
        KbArticle article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", id.toString()));
        articleRepository.delete(article);
    }

    private ArticleResponse mapToResponse(KbArticle article) {
        ArticleResponse response = new ArticleResponse();
        response.setId(article.getId());
        response.setTitle(article.getTitle());
        response.setContent(article.getContent());
        response.setExcerpt(article.getExcerpt());
        response.setStatus(article.getStatus());
        response.setAuthorId(article.getAuthorId());
        response.setViews(article.getViews());
        response.setHelpfulVotes(article.getHelpfulVotes());
        response.setCreatedAt(article.getCreatedAt());
        response.setUpdatedAt(article.getUpdatedAt());
        return response;
    }
}