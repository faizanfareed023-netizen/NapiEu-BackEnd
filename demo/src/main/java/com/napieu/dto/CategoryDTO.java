package com.napieu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long id;

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    private String slug;

    private String description;

    @Size(max = 7, message = "Color must be a hex code (e.g., #FF5733)")
    private String color;

    private Integer displayOrder;
}