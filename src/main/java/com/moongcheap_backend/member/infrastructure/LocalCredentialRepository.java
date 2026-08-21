package com.moongcheap_backend.member.infrastructure;

import com.moongcheap_backend.member.domain.LocalCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LocalCredentialRepository extends JpaRepository<LocalCredential, Long> {

    Optional<LocalCredential> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM LocalCredential c WHERE c.memberId = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);
}
