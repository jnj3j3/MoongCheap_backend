package com.moongcheap_backend.demand.application.demand;

import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;
import com.moongcheap_backend.demand.domain.demand.Demand;
import com.moongcheap_backend.demand.infrastructure.demand.DemandRepository;
import com.moongcheap_backend.demand.infrastructure.demandBoard.DemandBoardRepository;
import com.moongcheap_backend.demand.presentation.demand.dto.DemandCreateRequestDto;
import com.moongcheap_backend.product.domain.productCatalog.ProductCatalogStatus;
import com.moongcheap_backend.product.infrastructure.productCatalog.ProductCatalogRespository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DemandService {

    private final DemandRepository demandRepository;
    private final DemandBoardRepository demandBoardRepository;
    private final ProductCatalogRespository productCatalogRespository;

    @Transactional
    public Long create(DemandCreateRequestDto request, Long memberId) {
        // todo: request.payMethodId 를 통해 status 상태 확인

        // catalog 삭제의 경우 매우 드물게 일어난다는 가정 하에 lock 제약을 걸지 않음
        boolean hasCatalog = productCatalogRespository
            .existsByIdAndStatus(request.catalogId(), ProductCatalogStatus.ACTIVE);
        if (!hasCatalog) {
            throw new BusinessException(ErrorCode.PRODUCT_CATALOG_NOT_FOUND);
        }

        Demand demand = Demand.builder()
            .memberId(memberId)
            .catalogId(request.catalogId())
            .payMethodId(request.payMethodId())
            .desiredPriceMin(request.desiredPriceMin())
            .desiredPriceMax(request.desiredPriceMax())
            .desireEndAt(LocalDateTime.now().plusDays(7))
            .quantity(request.quantity())
            .extraRequirement(request.extraRequirement())
            .isSubstitutable(request.isSubstitutable())
            .build();

        return demandRepository.save(demand).getId();
    }

    // todo: 참여 취소


}
