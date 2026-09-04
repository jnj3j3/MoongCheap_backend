package com.moongcheap_backend.demand.infrastructure.demandBoard;

import com.moongcheap_backend.demand.domain.demandBoard.DemandBoard;
import com.moongcheap_backend.demand.domain.demandBoard.DemandBoardStatus;
import java.util.Collection;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DemandBoardRepository extends JpaRepository<DemandBoard, Long> {

    boolean existsByCatalogIdAndStatusIn(Long catalogId, Collection<DemandBoardStatus> statuses);

    @Modifying
    @Query("UPDATE DemandBoard db SET db.participantCount = db.participantCount - 1 WHERE db.id = :id AND db.participantCount > 0")
    int decrementParticipantCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE DemandBoard db SET db.participantCount = db.participantCount + 1 "
        + "WHERE db.id = :id AND db.status = :status AND db.saleEndAt > CURRENT_TIMESTAMP")
    int increaseParticipantCountIfActive(
        @Param("id") Long id, @Param("status") DemandBoardStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT db FROM DemandBoard db WHERE db.id = :id AND db.status IN :statuses")
    Optional<DemandBoard> findByIdAndStatusInForUpdate(@Param("id") Long id, @Param("statuses") Collection<DemandBoardStatus> statuses);
}
