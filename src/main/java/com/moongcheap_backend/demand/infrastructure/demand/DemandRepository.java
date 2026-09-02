package com.moongcheap_backend.demand.infrastructure.demand;

import com.moongcheap_backend.demand.domain.demand.Demand;
import com.moongcheap_backend.demand.domain.demand.DemandStatus;
import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface DemandRepository extends JpaRepository<Demand, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Demand> findAllByDemandBoardIdAndStatus(
        Long demandBoardId,
        DemandStatus status
    );
}
