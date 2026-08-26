package com.moongcheap_backend.groupbuy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//관심 공동 구매
@Entity
@Getter
@Table(name = "group_buy_likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupBuyLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "groupbuy_id")
    private Long groupBuyId;

    @Column(name = "member_id")
    private Long memberId;
}
