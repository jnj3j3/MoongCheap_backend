package com.moongcheap_backend.groupbuy.domain;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
public class GroupBuyLikesId implements Serializable {
    private Long groupBuyId;
    private Long memberId;
}
