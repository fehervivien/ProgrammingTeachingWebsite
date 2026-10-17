package com.ptw.progteaching.auth.filter;

import com.ptw.progteaching.auth.jwt.JwtTokenManager;
import com.ptw.progteaching.auth.model.SessionTokens;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenRefreshFilter implements Filter {

    private final JwtTokenManager jwtTokenManager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);

        if (session != null) {
            SessionTokens tokens = (SessionTokens) session.getAttribute("tokens");

            if (tokens != null && !jwtTokenManager.validateJwtToken(tokens.getAccessToken())) {
                if (jwtTokenManager.validateJwtToken(tokens.getRefreshToken())) {
                    String newAccessToken = jwtTokenManager.generateAccessToken(
                            tokens.getUsername(),
                            tokens.getUserId()
                    );
                    tokens.setAccessToken(newAccessToken);
                    session.setAttribute("tokens", tokens);
                } else {
                    session.invalidate();
                }
            }
        }

        chain.doFilter(request, response);
    }
}
