package com.moongcheap_backend.demand.application.demandBoard;

import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;
import com.moongcheap_backend.demand.domain.demand.Demand;
import com.moongcheap_backend.demand.domain.demand.DemandStatus;
import com.moongcheap_backend.demand.domain.demandBoard.DemandBoard;
import com.moongcheap_backend.demand.domain.demandBoard.DemandBoardStatus;
import com.moongcheap_backend.demand.infrastructure.demand.DemandRepository;
import com.moongcheap_backend.demand.infrastructure.demandBoard.DemandBoardQueryRepository;
import com.moongcheap_backend.demand.infrastructure.demandBoard.DemandBoardRepository;
import com.moongcheap_backend.demand.presentation.demandBoard.dto.AuctionResultDto;
import com.moongcheap_backend.demand.presentation.demandBoard.dto.CatalogDemandBoardListDto;
import com.moongcheap_backend.demand.presentation.demandBoard.dto.DemandBoardDto;
import com.moongcheap_backend.demand.presentation.demandBoard.dto.DemandBoardJoinRequestDto;
import com.moongcheap_backend.demand.presentation.demandBoard.dto.DemandBoardListDto;
import com.moongcheap_backend.demand.presentation.demandBoard.dto.DemandBoardSummaryDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DemandBoardService {

    private final DemandBoardRepository demandBoardRepository;
    private final DemandBoardQueryRepository demandBoardQueryRepository;
    private final DemandRepository demandRepository;

    @Transactional(readOnly = true)
    public boolean hasProductBoard(Long productBoardId) {
        return demandBoardRepository.existsByCatalogIdAndStatusIn(
            productBoardId,
            List.of(DemandBoardStatus.GB_ACTION_REQUIRED, DemandBoardStatus.GB_GATHERING));
    }

    @Transactional(readOnly = true)
    public DemandBoardListDto getHostDemandBoard(Pageable pageable) {
        return new DemandBoardListDto(demandBoardQueryRepository.getDemandBoardItems(
            List.of(DemandBoardStatus.GB_GATHERING), pageable));
    }

    @Transactional(readOnly = true)
    public DemandBoardDto getById(Long memberId, Long demandBoardId) {
        DemandBoardSummaryDto summary = demandBoardQueryRepository.getDemandBoardItemsById(
                demandBoardId)
            .orElseThrow(() -> new BusinessException(ErrorCode.DEMAND_BOARD_NOT_FOUND));
        boolean isParticipating = demandRepository.existsByMemberIdAndDemandBoardIdAndStatusIn(
            memberId, summary.demandBoardId(),
            List.of(DemandStatus.ASSIGNED, DemandStatus.PAYMENT_PENDING)
        );
        return DemandBoardDto.from(summary, isParticipating);
    }

    @Transactional(readOnly = true)
    public CatalogDemandBoardListDto getByCatalogId(
        Long memberId, Long catalogId, Pageable pageable, Integer minPrice, Integer maxPrice) {
        Pageable fetchPageable = PageRequest.of(
            pageable.getPageNumber(), pageable.getPageSize() + 1, pageable.getSort());
        List<CatalogDemandBoardListDto.DemandBoardCardDto> items =
            demandBoardQueryRepository.getDemandBoardsByCatalogId(
                catalogId, memberId, fetchPageable, minPrice, maxPrice);
        return CatalogDemandBoardListDto.of(items, pageable);
    }

    @Transactional(readOnly = true)
    public AuctionResultDto getAuctionResult(Long memberId, Long demandBoardId) {
        return demandBoardQueryRepository.getAuctionResult(demandBoardId, memberId)
            .orElseThrow(() -> new BusinessException(ErrorCode.DEMAND_BOARD_NOT_FOUND));
    }

    @Transactional
    public Long join(Long memberId, Long demandBoardId, DemandBoardJoinRequestDto request) {
        DemandBoard demandBoard = demandBoardRepository.findByIdAndStatusInForUpdate(demandBoardId,
                List.of(DemandBoardStatus.GB_GATHERING))
            .orElseThrow(() -> new BusinessException(ErrorCode.DEMAND_BOARD_NOT_FOUND));
        if (demandBoard.getSaleEndAt() == null
            || demandBoard.getSaleEndAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.DEMAND_BOARD_CLOSED);
        }
        //TODO: payMethodId 검증 로직
        Demand demand = Demand.boardJoinBuilder()
            .demandBoardId(demandBoard.getId())
            .memberId(memberId)
            .catalogId(demandBoard.getCatalogId())
            .payMethodId(request.payMethodId())
            .desiredPriceMin(demandBoard.getPriceMin())
            .desiredPriceMax(demandBoard.getPriceMax())
            .desireEndAt(demandBoard.getSaleEndAt())
            .quantity(request.quantity())
            .isSubstitutable(request.isSubstitutable())
            .extraRequirement(request.extraRequirement())
            .build();
        try {
            demandRepository.saveAndFlush(demand);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.DEMAND_ALREADY_EXISTS);
        }
        demandBoard.increaseParticipantCount();
        return demand.getId();
    }
}
