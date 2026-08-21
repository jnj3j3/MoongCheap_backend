package com.moongcheap_backend.auth.infrastructure.session;

import com.moongcheap_backend.common.security.SessionPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthSessionManager {

    public static final String PRINCIPAL_ATTR = "MOONGCHEAP_PRINCIPAL";
    public static final String MEMBER_INDEX_ATTR = FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME;
    public static final String GOOGLE_ACCESS_TOKEN_ATTR = "MOONGCHEAP_GOOGLE_ACCESS_TOKEN";

    private final SessionRepository<? extends Session> sessionRepository;
    private final FindByIndexNameSessionRepository<? extends Session> indexRepository;
    private final StringRedisTemplate redisTemplate;

    @Value("${moongcheap.security.session.absolute-ttl-default}")
    private Duration defaultTtl;

    @Value("${moongcheap.security.session.absolute-ttl-remember-me}")
    private Duration rememberMeTtl;

    public void bindPrincipal(HttpServletRequest request, SessionPrincipal principal, boolean rememberMe) {
        HttpSession existing = request.getSession(false);
        if(existing != null) {
            existing.invalidate();
        }
        var session = request.getSession(true);
        Duration ttl = rememberMe ? rememberMeTtl : defaultTtl;
        session.setMaxInactiveInterval((int) ttl.toSeconds());
        session.setAttribute(PRINCIPAL_ATTR, principal);
        session.setAttribute(MEMBER_INDEX_ATTR, String.valueOf(principal.memberId()));
    }

    public void refreshPrincipal(HttpServletRequest request, SessionPrincipal principal) {
        var session = request.getSession(false);
        if (session != null) {
            session.setAttribute(PRINCIPAL_ATTR, principal);
        }
    }

    public void invalidateCurrent(HttpServletRequest request) {
        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public void invalidateAllForMember(Long memberId) {
        Map<String, ? extends Session> sessions = indexRepository.findByPrincipalName(String.valueOf(memberId));
        for (String sessionId : sessions.keySet()) {
            sessionRepository.deleteById(sessionId);
        }
    }

    public void invalidateAllExceptCurrent(Long memberId, HttpServletRequest request) {
        HttpSession current = request.getSession(false);
        if (current == null) {
            throw new IllegalStateException("세션이 없는 상태에서 호출되었습니다.");
        }
        String currentId = current.getId();
        Map<String, ? extends Session> sessions = indexRepository.findByPrincipalName(String.valueOf(memberId));
        for (String sessionId : sessions.keySet()) {
            if (!sessionId.equals(currentId)) {
                sessionRepository.deleteById(sessionId);
            }
        }
    }
}
