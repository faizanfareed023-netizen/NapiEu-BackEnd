package com.napieu.controller;

import com.napieu.dto.ArticleDTO;
import com.napieu.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    // Public endpoints
    @GetMapping("/articles")
    public ResponseEntity<Page<ArticleDTO>> getAllPublishedArticles(
            @PageableDefault(size = 10, sort = "publishedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(articleService.getAllPublishedArticles(pageable));
    }

    @GetMapping("/articles/category/{categorySlug}")
    public ResponseEntity<Page<ArticleDTO>> getArticlesByCategory(
            @PathVariable String categorySlug,
            @PageableDefault(size = 10, sort = "publishedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(articleService.getArticlesByCategory(categorySlug, pageable));
    }

    @GetMapping("/articles/{slug}")
    public ResponseEntity<ArticleDTO> getArticleBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(articleService.getArticleBySlug(slug));
    }

    @PostMapping("/articles/{id}/view")
    public ResponseEntity<Void> incrementViews(@PathVariable Long id) {
        articleService.incrementViews(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/articles/search")
    public ResponseEntity<Page<ArticleDTO>> searchArticles(
            @RequestParam String q,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(articleService.searchArticles(q, pageable));
    }

    // Admin endpoints
    @GetMapping("/admin/articles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ArticleDTO>> getAllArticles(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(articleService.getAllArticles(pageable));
    }

    @PostMapping("/admin/articles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArticleDTO> createArticle(@Valid @RequestBody ArticleDTO articleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(articleService.createArticle(articleDTO));
    }

    @PutMapping("/admin/articles/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @Valid @RequestBody ArticleDTO articleDTO) {
        return ResponseEntity.ok(articleService.updateArticle(id, articleDTO));
    }

    @DeleteMapping("/admin/articles/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}