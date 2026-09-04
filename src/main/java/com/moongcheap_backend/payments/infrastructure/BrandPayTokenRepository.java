package com.moongcheap_backend.payments.infrastructure;

import com.moongcheap_backend.payments.domain.BrandPayToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandPayTokenRepository extends JpaRepository<BrandPayToken, Long> {

}
