package com.moongcheap_backend.category.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(example = """
        {
          "categoryIds": [1, 2, 3]
        }
        """)
public record InterestCategoryUpdateRequest(
        @NotEmpty List<Long> categoryIds
) {
}
