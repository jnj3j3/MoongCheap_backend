package com.moongcheap_backend.order.infrastructure;

import com.moongcheap_backend.order.domain.Orders;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Orders> findByOrderNoAndCustomer_Id(String orderNo, Long memberId);
}
