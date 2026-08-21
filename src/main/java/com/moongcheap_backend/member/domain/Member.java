package com.moongcheap_backend.member.domain;

import com.moongcheap_backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", length = 20)
    private String loginId;

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone_number", length = 512)
    private String phoneNumber;

    @Column(name = "is_seller", nullable = false)
    private boolean isSeller;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    private Member(String loginId, String nickname, String email, String phoneNumber) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isSeller = false;
    }

    public void changeProfile(String nickname, String email, String phoneNumber) {
        if (nickname != null) this.nickname = nickname;
        if (email != null) this.email = email;
        if (phoneNumber != null) this.phoneNumber = phoneNumber;
    }

    public void withdraw() {
        this.deletedAt = LocalDateTime.now();
        this.loginId = null;
        this.email = null;
        this.phoneNumber = null;
    }

    public void becomeSeller() {
        this.isSeller = true;
    }

    public void updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return deletedAt == null;
    }
}
