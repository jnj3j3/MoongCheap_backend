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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import javax.print.DocFlavor.STRING;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "brand_pay_method")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandPayMethod extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",  nullable = false)
    private Member member;

    //자동결제키
    @Column(name = "method_key", unique = true)
    private String methodKey;

    //은행/카드사 코드
    @Column(name = "provider_code")
    private ProviderCode providerCode;

    //계좌/카드번호
    @Column(name = "masked_number")
    private String maskedNumber;

    //결제수단 타입
    @Column(name = "type")
    private PaymentType type;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "status")
    private PaymentsMethodStatus status;

    //MVP에서만 사용
    //카드 유효기간
    @Column(name = "end_at")
    private LocalDateTime endAt;

    //카드 비밀번호 앞 2자리
    @Column(name = "password")
    private String password;

    //CVC번호
    @Column(name = "cvc_num")
    private String cvcNum;
}
