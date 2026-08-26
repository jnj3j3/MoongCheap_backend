package com.moongcheap_backend.payments.domain;

import com.moongcheap_backend.common.entity.BaseTimeEntity;
import com.moongcheap_backend.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//브랜드페이에서 사용자가 작업할 때 필요함.
@Entity
@Getter
@Table(name = "brand_pay_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandPayToken extends BaseTimeEntity {

    @Id
    private Long memberId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "access_token_expires_at")
    private LocalDateTime accessTokenExpiresAt;
}
