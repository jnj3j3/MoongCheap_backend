package com.moongcheap_backend.category.application;

import com.moongcheap_backend.category.domain.Category;
import com.moongcheap_backend.category.infrastructure.CategoryRepository;
import com.moongcheap_backend.category.presentation.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {
        Map<Long, List<Category>> byParent = categoryRepository.findAllByIsActiveTrue()
                .stream()
                .collect(Collectors.groupingBy(
                        c -> c.getParentId() == null ? 0L : c.getParentId()
                ));

        return byParent.getOrDefault(0L, List.of()).stream()
                .map(root -> toResponse(root, byParent))
                .toList();
    }

    private CategoryResponse toResponse(Category category, Map<Long, List<Category>> byParent) {
        List<CategoryResponse> children = byParent.getOrDefault(category.getId(), List.of())
                .stream()
                .map(child -> toResponse(child, byParent))
                .toList();
        return CategoryResponse.of(category, children);
    }
}
