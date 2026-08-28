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
    @Query("UPDATE ShippingAddress a SET a.isDefault = false " +
            "WHERE a.memberId = :memberId AND a.isDefault = true")
    int unmarkAllDefaults(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE ShippingAddress a SET a.isDefault = false " +
            "WHERE a.memberId = :memberId AND a.isDefault = true AND a.id <> :excludeId")
    int unmarkDefaultExcept(@Param("memberId") Long memberId, @Param("excludeId") Long excludeId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE ShippingAddress a SET a.isDefault = true " +
            "WHERE a.id = :addressId AND a.memberId = :memberId")
    int setAsDefault(@Param("addressId") Long addressId, @Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE shipping_address SET is_default = true " +
            "WHERE id = (SELECT id FROM shipping_address " +
            "            WHERE member_id = :memberId " +
            "            ORDER BY created_at ASC LIMIT 1) " +
            "  AND NOT EXISTS (SELECT 1 FROM shipping_address " +
            "                  WHERE member_id = :memberId AND is_default = true)",
            nativeQuery = true)
    int promoteOldestIfNoDefault(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM ShippingAddress a WHERE a.id = :addressId AND a.memberId = :memberId")
    int deleteByIdAndMemberId(@Param("addressId") Long addressId, @Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM ShippingAddress a WHERE a.memberId = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);
}
