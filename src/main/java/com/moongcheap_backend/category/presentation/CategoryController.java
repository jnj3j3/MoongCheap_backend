package com.moongcheap_backend.category.presentation;

import com.moongcheap_backend.category.application.CategoryService;
import com.moongcheap_backend.category.presentation.dto.CategoryResponse;
import com.moongcheap_backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Category", description = "카테고리 조회")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "전체 카테고리 조회", description = "활성화된 카테고리를 트리 구조로 반환.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> list() {
        return ResponseEntity.ok(ApiResponse.ok(categoryService.getAll()));
    }
}
