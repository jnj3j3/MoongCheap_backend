package com.moongcheap_backend.groupbuy.domain;

import com.moongcheap_backend.common.entity.BaseTimeEntity;
import com.moongcheap_backend.member.domain.Seller;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder.Default;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    //product 테이블 추가시 외래키 전환
    @Column(name = "product_id", nullable = false)
    private Long productId;

    //판매페이지이름
    @Column(name = "title", nullable = false)
    private String title;

    //목표 인원수
    @Column(name = "target_count",  nullable = false)
    private Integer targetCount;

    //현재 참여 인원수
    @Column(name = "count",  nullable = false)
    private Integer count;

    //공동구매 만료 일시
    @Column(name = "group_buy_end_at")
    private LocalDateTime groupBuyEndAt;

    //공동구매 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private GroupBuyStatus status;
}
