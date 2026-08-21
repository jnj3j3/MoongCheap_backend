package com.moongcheap_backend.member.infrastructure;

import com.moongcheap_backend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginIdAndDeletedAtIsNull(String loginId);
    Optional<Member> findByIdAndDeletedAtIsNull(Long id);
    boolean existsByLoginIdAndDeletedAtIsNull(String loginId);
    boolean existsByNicknameAndDeletedAtIsNull(String nickname);

    @Query("SELECT m.nickname FROM Member m WHERE m.nickname IN :nicknames AND m.deletedAt IS NULL")
    Set<String> findTakenNicknames(@Param("nicknames") Collection<String> nicknames);
}
