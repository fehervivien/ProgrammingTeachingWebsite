package com.ptw.progteaching.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenManager {

    private static final String USER_ID_CLAIM_NAME = "userId";
    private static final String TOKEN_TYPE_CLAIM_NAME = "type";

    private final SecretKey secretKey;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JwtTokenManager(@Value("${jwt.secret}") String jwtSecretKey,
                           @Value("${jwt.access-expiration-ms}") long accessTokenExpirationMs,
                           @Value("${jwt.refresh-expiration-ms}") long refreshTokenExpirationMs) {
        secretKey = Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public String generateAccessToken(String username, Long userId) {
        return Jwts.builder()
                .subject(username)
                .claim(USER_ID_CLAIM_NAME, userId)
                .claim(TOKEN_TYPE_CLAIM_NAME, "access")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpirationMs))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String username, Long userId) {
        return Jwts.builder()
                .subject(username)
                .claim(USER_ID_CLAIM_NAME, userId)
                .claim(TOKEN_TYPE_CLAIM_NAME, "refresh")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMs))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (Exception _) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Long extractUserId(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(USER_ID_CLAIM_NAME, Long.class);
    }
}
