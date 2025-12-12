package com.napieu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 500, message = "Title must be less than 500 characters")
    private String title;

    private String slug;

    @NotBlank(message = "Content is required")
    private String content;

    private String excerpt;

    private String featuredImage;

    private String author;

    private Integer readTime;

    private Integer views;

    private Boolean published;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime publishedAt;

    private String source;

    private Set<Long> categoryIds;

    private Set<CategoryDTO> categories;
}