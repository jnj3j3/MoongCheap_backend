package com.moongcheap_backend.groupbuy.application;

import com.moongcheap_backend.groupbuy.domain.GroupBuy;
import com.moongcheap_backend.groupbuy.domain.GroupBuyStatus;
import com.moongcheap_backend.groupbuy.infrastructure.GroupBuyRepository;
import com.moongcheap_backend.groupbuy.presentation.dto.GroupBuyDetailResponse;
import com.moongcheap_backend.groupbuy.presentation.dto.GroupBuyListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupBuyService {

    private final GroupBuyRepository groupBuyRepository;

    @Transactional(readOnly = true)
    public Page<GroupBuyListResponse> getAll(Pageable pageable) {
        return groupBuyRepository.findAllByStatus(GroupBuyStatus.OPEN, pageable)
            .map(this::toListResponse);
    }

    public GroupBuyDetailResponse getById(Long memberId, Long groupBuyId) {
        throw new UnsupportedOperationException("공동구매 상세 조회는 구현 예정입니다.");
    }

    private GroupBuyListResponse toListResponse(GroupBuy groupBuy) {
        return new GroupBuyListResponse(
            groupBuy.getTitle(),
            groupBuy.getProduct().getThumbnailUrl(),
            groupBuy.getProduct().getUnitPrice()
        );
    }
}
