package com.napieu.service;

import com.napieu.dto.ArticleDTO;
import com.napieu.exception.ResourceNotFoundException;
import com.napieu.model.Article;
import com.napieu.model.Category;
import com.napieu.repository.ArticleRepository;
import com.napieu.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    public Page<ArticleDTO> getAllPublishedArticles(Pageable pageable) {
        return articleRepository.findByPublishedTrueOrderByPublishedAtDesc(pageable)
                .map(this::convertToDTO);
    }

    public Page<ArticleDTO> getArticlesByCategory(String categorySlug, Pageable pageable) {
        return articleRepository.findByCategory_SlugAndPublishedTrue(categorySlug, pageable)
                .map(this::convertToDTO);
    }

    public ArticleDTO getArticleBySlug(String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with slug: " + slug));

        if (!article.getPublished()) {
            throw new ResourceNotFoundException("Article is not published");
        }

        return convertToDTO(article);
    }

    @Transactional
    public void incrementViews(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        article.incrementViews();
        articleRepository.save(article);
    }

    public Page<ArticleDTO> searchArticles(String query, Pageable pageable) {
        return articleRepository.searchArticles(query, pageable)
                .map(this::convertToDTO);
    }

    // Admin methods
    public Page<ArticleDTO> getAllArticles(Pageable pageable) {
        return articleRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::convertToDTO);
    }

    @Transactional
    public ArticleDTO createArticle(ArticleDTO articleDTO) {
        Article article = new Article();
        updateArticleFromDTO(article, articleDTO);

        Article savedArticle = articleRepository.save(article);
        return convertToDTO(savedArticle);
    }

    @Transactional
    public ArticleDTO updateArticle(Long id, ArticleDTO articleDTO) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        updateArticleFromDTO(article, articleDTO);

        Article updatedArticle = articleRepository.save(article);
        return convertToDTO(updatedArticle);
    }

    @Transactional
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        articleRepository.delete(article);
    }

    private void updateArticleFromDTO(Article article, ArticleDTO dto) {
        article.setTitle(dto.getTitle());
        article.setSlug(dto.getSlug() != null ? dto.getSlug() : generateSlug(dto.getTitle()));
        article.setContent(dto.getContent());
        article.setExcerpt(dto.getExcerpt());
        article.setFeaturedImage(dto.getFeaturedImage());
        article.setAuthor(dto.getAuthor());
        article.setReadTime(dto.getReadTime());
        article.setPublished(dto.getPublished() != null ? dto.getPublished() : false);
        article.setSource(dto.getSource());

        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            Set<Category> categories = new HashSet<>();
            for (Long categoryId : dto.getCategoryIds()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
                categories.add(category);
            }
            article.setCategories(categories);
        }
    }

    private ArticleDTO convertToDTO(Article article) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setSlug(article.getSlug());
        dto.setContent(article.getContent());
        dto.setExcerpt(article.getExcerpt());
        dto.setFeaturedImage(article.getFeaturedImage());
        dto.setAuthor(article.getAuthor());
        dto.setReadTime(article.getReadTime());
        dto.setViews(article.getViews());
        dto.setPublished(article.getPublished());
        dto.setCreatedAt(article.getCreatedAt());
        dto.setUpdatedAt(article.getUpdatedAt());
        dto.setPublishedAt(article.getPublishedAt());
        dto.setSource(article.getSource());

        if (article.getCategories() != null) {
            dto.setCategoryIds(article.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    private String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[áàâä]", "a")
                .replaceAll("[éèêë]", "e")
                .replaceAll("[íìîï]", "i")
                .replaceAll("[óòôö]", "o")
                .replaceAll("[úùûü]", "u")
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}