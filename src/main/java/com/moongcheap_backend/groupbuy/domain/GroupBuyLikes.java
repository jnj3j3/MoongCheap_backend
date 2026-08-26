package com.moongcheap_backend.groupbuy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//관심 공동 구매
@Entity
@Getter
@Table(name = "group_buy_likes")
@IdClass(GroupBuyLikesId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupBuyLikes {
    @Id
    @Column(name = "group_buy_id")
    private Long groupBuyId;

    @Id
    @Column(name = "member_id")
    private Long memberId;
}
