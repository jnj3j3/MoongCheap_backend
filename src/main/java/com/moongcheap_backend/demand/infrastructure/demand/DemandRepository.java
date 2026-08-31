package com.moongcheap_backend.demand.infrastructure.demand;

import com.moongcheap_backend.demand.domain.demand.Demand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandRepository extends JpaRepository<Demand, Long> {
}
