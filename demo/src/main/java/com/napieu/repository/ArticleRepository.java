package com.napieu.repository;

import com.napieu.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findBySlug(String slug);

    Boolean existsBySlug(String slug);

    // Find all published articles ordered by published date
    Page<Article> findByPublishedTrueOrderByPublishedAtDesc(Pageable pageable);

    // Find articles by category slug
    @Query("SELECT a FROM Article a JOIN a.categories c WHERE c.slug = :categorySlug AND a.published = true ORDER BY a.publishedAt DESC")
    Page<Article> findByCategory_SlugAndPublishedTrue(@Param("categorySlug") String categorySlug, Pageable pageable);

    // Search articles by title or content
    @Query("SELECT a FROM Article a WHERE a.published = true AND (LOWER(a.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(a.content) LIKE LOWER(CONCAT('%', :query, '%'))) ORDER BY a.publishedAt DESC")
    Page<Article> searchArticles(@Param("query") String query, Pageable pageable);

    // Get latest published articles
    Page<Article> findTop10ByPublishedTrueOrderByPublishedAtDesc(Pageable pageable);

    // Get all articles for admin (published and unpublished)
    Page<Article> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Count published articles
    Long countByPublishedTrue();
}