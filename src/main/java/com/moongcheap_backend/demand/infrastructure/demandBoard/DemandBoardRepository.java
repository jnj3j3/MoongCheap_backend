package com.moongcheap_backend.demand.infrastructure.demandBoard;

import com.moongcheap_backend.demand.domain.demandBoard.DemandBoard;
import com.moongcheap_backend.demand.domain.demandBoard.DemandBoardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface DemandBoardRepository extends JpaRepository<DemandBoard, Long> {

    boolean existsByCatalogIdAndStatusIn(Long catalogId, Collection<DemandBoardStatus> statuses);
}
