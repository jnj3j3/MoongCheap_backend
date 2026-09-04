package com.moongcheap_backend.payments.infrastructure;

import com.moongcheap_backend.payments.domain.CustomerKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerKeyRepository extends JpaRepository<CustomerKey, Long> {

}
