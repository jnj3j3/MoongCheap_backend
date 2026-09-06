package com.moongcheap_backend.groupbuy.infrastructure;

import com.moongcheap_backend.groupbuy.domain.GroupBuy;
import com.moongcheap_backend.groupbuy.domain.GroupBuyLikes;
import com.moongcheap_backend.groupbuy.domain.GroupBuyLikesId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupBuyLikesRepository extends JpaRepository<GroupBuyLikes, GroupBuyLikesId> {

}
