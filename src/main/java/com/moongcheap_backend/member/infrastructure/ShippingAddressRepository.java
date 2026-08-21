package com.moongcheap_backend.member.infrastructure;

import com.moongcheap_backend.member.domain.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {

    List<ShippingAddress> findAllByMemberIdOrderByIsDefaultDescCreatedAtDesc(Long memberId);

    long countByMemberId(Long memberId);

    Optional<ShippingAddress> findByMemberIdAndIsDefaultTrue(Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM ShippingAddress a WHERE a.id = :addressId AND a.memberId = :memberId")
    int deleteByIdAndMemberId(@Param("addressId") Long addressId, @Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM ShippingAddress a WHERE a.memberId = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);
}
