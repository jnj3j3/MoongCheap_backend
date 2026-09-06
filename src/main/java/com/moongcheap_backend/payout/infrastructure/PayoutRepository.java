package com.moongcheap_backend.payout.infrastructure;

import com.moongcheap_backend.payout.domain.Payout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayoutRepository extends JpaRepository<Payout, Long> {

}
