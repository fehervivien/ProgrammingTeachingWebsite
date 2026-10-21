package com.ptw.progteaching.common.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final SessionAuthenticationStrategy sessionStrategy;
    private final SecurityContextRepository repository = new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy holder = SecurityContextHolder.getContextHolderStrategy();

    // TODO Később JWT-t kipróbálni de lehet nem lesz rá szükség
    public void startSession(Authentication auth) {
        request.getSession(true);
        sessionStrategy.onAuthentication(auth, request, response);

        SecurityContext context = holder.createEmptyContext();
        context.setAuthentication(auth);
        holder.setContext(context);
        repository.saveContext(context, request, response);
    }

    public void invalidateSession(HttpSession session) {
        session.invalidate();
    }
}
