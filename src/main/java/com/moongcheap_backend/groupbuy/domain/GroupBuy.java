package com.moongcheap_backend.groupbuy.domain;

import com.moongcheap_backend.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "group_buy")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupBuy extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    //판매페이지이름
    @Column(name = "title", nullable = false)
    private String title;

    //목표 인원수
    @Column(name = "target_count")
    private Integer targetCount;

    //현재 참여 인원수
    @Column(name = "count")
    private Integer count;

    //공동구매 만료 일시
    @Column(name = "group_buy_end_at")
    private LocalDateTime groupBuyEndAt;

    //공동구매 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private GroupBuyStatus status;
}
