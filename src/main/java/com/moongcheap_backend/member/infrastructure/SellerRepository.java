package com.moongcheap_backend.member.infrastructure;

import com.moongcheap_backend.member.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findByIdAndDeletedAtIsNull(Long id);

    Optional<Seller> findByMemberIdAndDeletedAtIsNull(Long memberId);

    boolean existsByMemberIdAndDeletedAtIsNull(Long memberId);

    boolean existsByBusinessNumberHashAndDeletedAtIsNull(String businessNumberHash);
}
