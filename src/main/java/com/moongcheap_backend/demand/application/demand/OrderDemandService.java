package com.moongcheap_backend.demand.application.demand;

import com.moongcheap_backend.demand.domain.demand.Demand;
import com.moongcheap_backend.demand.domain.demand.DemandStatus;
import com.moongcheap_backend.demand.infrastructure.demand.DemandRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderDemandService {

    private final DemandRepository demandRepository;

    @Transactional
    public List<Demand> getPaymentPendingForOrder(Long demandBoardId) {
        return demandRepository.findAllByDemandBoardIdAndStatus(
            demandBoardId,
            DemandStatus.PAYMENT_PENDING
        );
    }
}
