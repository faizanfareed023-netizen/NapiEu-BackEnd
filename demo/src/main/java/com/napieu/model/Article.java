package com.napieu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(unique = true, nullable = false, length = 500)
    private String slug;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String excerpt;

    @Column(name = "featured_image", length = 500)
    private String featuredImage;

    @Column(length = 100)
    private String author = "NapiEU";

    @Column(name = "read_time")
    private Integer readTime; // in minutes

    @Column(nullable = false)
    private Integer views = 0;

    @Column(nullable = false)
    private Boolean published = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(length = 200)
    private String source; // e.g., "Euractiv"

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "article_categories",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (published && publishedAt == null) {
            publishedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (published && publishedAt == null) {
            publishedAt = LocalDateTime.now();
        }
    }

    public void incrementViews() {
        this.views++;
    }
}