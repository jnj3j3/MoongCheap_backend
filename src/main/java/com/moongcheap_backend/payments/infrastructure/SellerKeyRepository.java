package com.moongcheap_backend.payments.infrastructure;

import com.moongcheap_backend.payments.domain.SellerKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerKeyRepository extends JpaRepository<SellerKey, Long> {

}
