package com.moongcheap_backend.demand.domain.demandBoard;

import com.moongcheap_backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "demand_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DemandBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "catalog_id", nullable = false)
    private Long catalogId;

    @Column(name = "participant_count", nullable = false)
    private int participantCount = 0;

    @Column(name = "price_min")
    private Integer priceMin;

    @Column(name = "price_max")
    private Integer priceMax;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private DemandBoardStatus status = DemandBoardStatus.GB_GATHERING;

    @Column(name = "judged_at")
    private LocalDateTime judgedAt;

    @Column(name = "sale_end_at", nullable = false)
    private LocalDateTime saleEndAt;
}
