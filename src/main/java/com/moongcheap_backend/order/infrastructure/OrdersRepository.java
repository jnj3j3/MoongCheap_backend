package com.moongcheap_backend.order.infrastructure;

import com.moongcheap_backend.order.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

}
