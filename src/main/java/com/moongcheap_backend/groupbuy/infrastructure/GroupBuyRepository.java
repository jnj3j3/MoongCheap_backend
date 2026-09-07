package com.moongcheap_backend.groupbuy.infrastructure;

import com.moongcheap_backend.groupbuy.domain.GroupBuy;
import com.moongcheap_backend.groupbuy.domain.GroupBuyStatus;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupBuyRepository extends JpaRepository<GroupBuy, Long> {

    @EntityGraph(attributePaths = "product")
    Page<GroupBuy> findAllByStatus(GroupBuyStatus status, Pageable pageable);

    @EntityGraph(attributePaths = "product")
    Optional<GroupBuy> findDetailById(Long groupBuyId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select gb
        from GroupBuy gb
        join fetch gb.seller
        join fetch gb.product
        where gb.id = :groupBuyId
        """)
    Optional<GroupBuy> findByIdWithSellerAndProductForUpdate(
        @Param("groupBuyId") Long groupBuyId
    );
}
