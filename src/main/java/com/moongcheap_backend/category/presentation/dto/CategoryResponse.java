package com.moongcheap_backend.category.presentation.dto;

import com.moongcheap_backend.category.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(example = """
        {
          "id": 1,
          "name": "의류",
          "children": [
            { "id": 4, "name": "상의", "children": [] },
            { "id": 5, "name": "하의", "children": [] }
          ]
        }
        """)
public record CategoryResponse(
        Long id,
        String name,
        List<CategoryResponse> children
) {
    public static CategoryResponse of(Category category, List<CategoryResponse> children) {
        return new CategoryResponse(category.getId(), category.getName(), children);
    }
}
