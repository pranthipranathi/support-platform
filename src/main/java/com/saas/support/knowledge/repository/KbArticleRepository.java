package com.saas.support.knowledge.repository;

import com.saas.support.knowledge.entity.KbArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KbArticleRepository extends JpaRepository<KbArticle, UUID> {

    List<KbArticle> findByStatus(String status);

    List<KbArticle> findByTitleContainingIgnoreCase(String keyword);
}