package com.moongcheap_backend.demand.application.demandBoard;

import com.moongcheap_backend.demand.domain.demandBoard.DemandBoardStatus;
import com.moongcheap_backend.demand.infrastructure.demandBoard.DemandBoardRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DemandBoardService {
    //todo: catalog id를 이용한 수요 확인

    private final DemandBoardRepository demandBoardRepository;

    @Transactional(readOnly = true)
    public boolean hasProductBoard(Long productBoardId) {
        return demandBoardRepository.existsByCatalogIdAndStatusIn(
            productBoardId,
            List.of(DemandBoardStatus.GB_ACTION_REQUIRED, DemandBoardStatus.GB_GATHERING));
    }

    public void list() {

    }

}
