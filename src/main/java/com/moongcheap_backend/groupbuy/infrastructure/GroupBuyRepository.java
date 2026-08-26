package com.moongcheap_backend.groupbuy.infrastructure;

import com.moongcheap_backend.groupbuy.domain.GroupBuy;
import com.moongcheap_backend.member.domain.LocalCredential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupBuyRepository extends JpaRepository<GroupBuy, Long> {

}
