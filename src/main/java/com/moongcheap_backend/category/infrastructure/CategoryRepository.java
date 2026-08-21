package com.moongcheap_backend.category.infrastructure;

import com.moongcheap_backend.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByIsActiveTrue();
    List<Category> findAllByParentIdIsNullAndIsActiveTrue();
    List<Category> findAllByParentIdAndIsActiveTrue(Long parentId);
}
