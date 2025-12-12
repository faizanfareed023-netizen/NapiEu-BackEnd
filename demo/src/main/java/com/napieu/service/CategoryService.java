package com.napieu.service;

import com.napieu.dto.CategoryDTO;
import com.napieu.exception.ResourceNotFoundException;
import com.napieu.model.Category;
import com.napieu.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with slug: " + slug));
        return convertToDTO(category);
    }

    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setSlug(categoryDTO.getSlug() != null ? categoryDTO.getSlug() : generateSlug(categoryDTO.getName()));
        category.setDescription(categoryDTO.getDescription());
        category.setColor(categoryDTO.getColor());
        category.setDisplayOrder(categoryDTO.getDisplayOrder() != null ? categoryDTO.getDisplayOrder() : 0);

        Category savedCategory = categoryRepository.save(category);
        return convertToDTO(savedCategory);
    }

    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        category.setName(categoryDTO.getName());
        category.setSlug(categoryDTO.getSlug());
        category.setDescription(categoryDTO.getDescription());
        category.setColor(categoryDTO.getColor());
        category.setDisplayOrder(categoryDTO.getDisplayOrder());

        Category updatedCategory = categoryRepository.save(category);
        return convertToDTO(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        categoryRepository.delete(category);
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setDescription(category.getDescription());
        dto.setColor(category.getColor());
        dto.setDisplayOrder(category.getDisplayOrder());
        return dto;
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[áàâä]", "a")
                .replaceAll("[éèêë]", "e")
                .replaceAll("[íìîï]", "i")
                .replaceAll("[óòôö]", "o")
                .replaceAll("[úùûü]", "u")
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}