package com.moongcheap_backend.member.infrastructure;

import com.moongcheap_backend.member.domain.SocialCredential;
import com.moongcheap_backend.member.domain.SocialProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SocialCredentialRepository extends JpaRepository<SocialCredential, Long> {
    Optional<SocialCredential> findByProviderAndProviderId(SocialProvider provider, String providerId);
    List<SocialCredential> findAllByMemberId(Long memberId);
    long countByMemberId(Long memberId);
    Optional<SocialCredential> findByMemberIdAndProvider(Long memberId, SocialProvider provider);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM SocialCredential c WHERE c.memberId = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);
}
