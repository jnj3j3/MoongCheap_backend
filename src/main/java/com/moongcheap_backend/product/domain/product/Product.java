package com.moongcheap_backend.product.domain.product;

import com.moongcheap_backend.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "demand_board_id", nullable = false)
    private Long demandBoardId;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "thumbnail_url", length = 255)
    private String thumbnailUrl;

    @Column(name = "unit_price", nullable = false)
    private Integer unitPrice;

    @Column(name = "shipping_fee", nullable = false)
    private Integer shippingFee;

    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ProductStatus status;

    @Builder
    private Product(
        Long demandBoardId,
        Long sellerId,
        String thumbnailUrl,
        Integer unitPrice,
        Integer shippingFee,
        Integer totalQuantity,
        ProductStatus status
    ) {
        this.demandBoardId = demandBoardId;
        this.sellerId = sellerId;
        this.thumbnailUrl = thumbnailUrl;
        this.unitPrice = unitPrice;
        this.shippingFee = shippingFee;
        this.totalQuantity = totalQuantity;
        this.status = ProductStatus.BIDDING;
    }

    public boolean isAwarded() {
        return status == ProductStatus.AWARDED;
    }
}
