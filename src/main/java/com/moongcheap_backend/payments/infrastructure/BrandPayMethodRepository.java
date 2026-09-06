package com.moongcheap_backend.payments.infrastructure;

import com.moongcheap_backend.payments.domain.BrandPayMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandPayMethodRepository extends JpaRepository<BrandPayMethod, Long> {

}
