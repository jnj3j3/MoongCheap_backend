package com.moongcheap_backend.category.domain;

import com.moongcheap_backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "depth", nullable = false)
    private Short depth;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Builder
    private Category(Long parentId, String name, Short depth) {
        this.parentId = parentId;
        this.name = name;
        this.depth = depth;
        this.isActive = true;
    }
}
