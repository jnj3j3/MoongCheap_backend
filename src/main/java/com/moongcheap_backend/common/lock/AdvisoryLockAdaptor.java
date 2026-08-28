package com.moongcheap_backend.common.lock;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class AdvisoryLockAdaptor {

    @PersistenceContext
    private EntityManager em;

    /**
     * lock_timeout 설정 후 트랜잭션 범위 Advisory Lock 획득.
     * timeout 초과 시 PostgreSQL이 55P03을 발생시키며 트랜잭션이 롤백된다.
     */
    public void acquireXactLock(String lockKey, String timeout) {
        setLockTimeout(timeout);
        em.createNativeQuery(
                        "SELECT pg_advisory_xact_lock(" +
                                "('x' || substr(md5(:k), 1, 16))::bit(64)::bigint" +
                                ")"
                )
                .setParameter("k", lockKey)
                .getSingleResult();
    }

    private void setLockTimeout(String value) {
        em.createNativeQuery("SET LOCAL lock_timeout = '" + sanitize(value) + "'")
                .executeUpdate();
    }

    private String sanitize(String value) {
        if (!value.matches("^[0-9]+(ms|s|min)?$")) {
            throw new IllegalArgumentException(
                    "Invalid lock_timeout value: " + value + " (expected like '3s', '500ms')");
        }
        return value;
    }
}
